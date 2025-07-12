package com.tienhuynh.auth_service.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienhuynh.auth_service.payload.OAuth2Request;
import com.tienhuynh.auth_service.dto.UserDTO;
import com.tienhuynh.auth_service.oauth2.FacebookRegistryProperties;
import com.tienhuynh.auth_service.oauth2.GoogleRegistryProperties;
import com.tienhuynh.auth_service.payload.RegisterRequest;
import com.tienhuynh.auth_service.rabbitmq.RabbitMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Service
public class OAuth2Service {
    @Autowired
    private GoogleRegistryProperties googleProperties;

    @Autowired
    private FacebookRegistryProperties facebookProperties;

    @Autowired
    private RabbitMQProducer rabbitMQProducer;

    @Autowired
    private AuthService authService;

    @Autowired
    private ObjectMapper jsonObjectMapper;

    @Autowired
    private RestTemplate restTemplate;

    public ResponseEntity<?> generateAuthUrl(String loginType) {
        if (loginType.equals("google")) {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder
                    .fromUriString(googleProperties.getAuthorizationUri())
                    .queryParam("client_id", googleProperties.getClientId())
                    .queryParam("redirect_uri", googleProperties.getRedirectUri().replace("{baseUrl}", "http://localhost:9001"))
                    .queryParam("response_type", "code")
                    .queryParam("scope", String.join(" ", googleProperties.getScope()));
            return ResponseEntity.ok(uriBuilder.toUriString());
        }
        else if (loginType.equals("facebook")) {
            UriComponentsBuilder uriBuilder = UriComponentsBuilder
                    .fromUriString(facebookProperties.getAuthorizationUri())
                    .queryParam("client_id", facebookProperties.getClientId())
                    .queryParam("redirect_uri", facebookProperties.getRedirectUri().replace("{baseUrl}", "http://localhost:9001"))
                    .queryParam("response_type", "code")
                    .queryParam("scope", String.join(" ", facebookProperties.getScope()));
            return ResponseEntity.ok(uriBuilder.toUriString());
        }
        return ResponseEntity.badRequest().body("Invalid Login Type");
    }

    public ResponseEntity<?> handleProviderCallBack(OAuth2Request req) {
        RegisterRequest metaData = fetchUserInfo(req);

        if (metaData == null) {
            return ResponseEntity.badRequest().body("Failed to authenticate user info.");
        }

        req.setLoginType(req.getLoginType().toLowerCase().trim());
        metaData.setRole(req.getRole());

        // Gọi User-Service để check user theo email
        String foundUserJson = rabbitMQProducer.getUser(metaData.getMail());

        // ✅ Nếu user tồn tại
        if (!foundUserJson.equals("ERROR: User not found")) {
            try {
                UserDTO user = jsonObjectMapper.readValue(foundUserJson, UserDTO.class);

                // Nếu chưa gán sub trước đó → gán lần đầu
                if (user.getSub() == null || user.getSub().isEmpty()) {
                    user.setSub(metaData.getSub());
                    String updateResp = rabbitMQProducer.updateUser(user);
                    if (!"SUCCESSFULLY UPDATED".equals(updateResp)) {
                        return ResponseEntity.badRequest().body(updateResp);
                    }
                }

                // Nếu sub không khớp → từ chối
                if (!user.getSub().equals(metaData.getSub())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("This email has already been used. Please log in using the originally linked provider.");
                }

                // Đúng sub → login thành công
                return ResponseEntity.ok(authService.generateToken(
                        user.getMail(), user.getRole(), "Successfully logged in"
                ));

            } catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Failed to parse user info.");
            }
        }

        // User chưa tồn tại → tạo mới
        metaData.setPwd_hash("Password123@");
        metaData.setProfile(new HashMap<>());
        metaData.setVerified_status("VERIFIED");
        return ResponseEntity.ok(authService.register(metaData));
    }


    private RegisterRequest fetchUserInfo(OAuth2Request req) {
        String clientId = "";
        String clientSecret = "";
        String redirectUri = "";
        String getTokenUrl = "";
        String getUserInfoUrl = "";

        // Set config for each  provider
        if (req.getLoginType().equals("google")) {
            clientId = googleProperties.getClientId();
            clientSecret = googleProperties.getClientSecret();
            redirectUri = googleProperties.getRedirectUri();
            getTokenUrl = googleProperties.getTokenUri();
            getUserInfoUrl = googleProperties.getUserInfoUri();
        }
        else if (req.getLoginType().equals("facebook")) {
            clientId = facebookProperties.getClientId();
            clientSecret = facebookProperties.getClientSecret();
            redirectUri = facebookProperties.getRedirectUri();
            getTokenUrl = facebookProperties.getTokenUri();
            getUserInfoUrl = facebookProperties.getUserInfoUri();
        }
        else {
            return null;
        }

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setRole(req.getRole());

        // Get AccessToken from provided code
        String accessToken = getAccesToken(req.getCode(), clientId, clientSecret, redirectUri, getTokenUrl);

        // Fetch User Info from Provided Access Token
        Map userInfo = getUserInfo(
                getUserInfoUrl,
                accessToken,
                req.getLoginType()
        );

        // Gain value
        registerRequest.setRole(req.getRole());
        registerRequest.setMail(userInfo.get("email").toString());
        registerRequest.setFull_name(userInfo.get("name").toString());

        if (req.getLoginType().equals("facebook")) {
            registerRequest.setSub("facebook-" + userInfo.get("id").toString());
        }
        else if (req.getLoginType().equals("google")) {
            registerRequest.setSub("google-" + userInfo.get("sub").toString());
        }

        return registerRequest;
    }

    private String getAccesToken(
            String code,
            String clientId,
            String clientSecret,
            String redirectUri,
            String getTokenUrl
    ) {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<?> tokenRequest = new HttpEntity<>(params, headers);

        // Formatting Response
        ResponseEntity<Map> tokenResponse = restTemplate.exchange(
                getTokenUrl,
                HttpMethod.POST,
                tokenRequest,
                Map.class
        );

        return tokenResponse.getBody().get("access_token").toString();
    }

    private Map getUserInfo(
            String getUserInfoUrl,
            String accessToken,
            String loginType
    ) {
        switch (loginType)  {
            case "google":
                HttpHeaders userHeaders = new HttpHeaders();
                userHeaders.setBearerAuth(accessToken);
                HttpEntity<?> userRequest = new HttpEntity<>(userHeaders);

                return restTemplate.exchange(
                        getUserInfoUrl,
                        HttpMethod.GET,
                        userRequest,
                        Map.class
                ).getBody();

                case "facebook":
                    String userUrl = UriComponentsBuilder.fromHttpUrl(getUserInfoUrl)
                            .queryParam("fields", "id,name,email,picture")
                            .queryParam("access_token", accessToken)
                            .toUriString();
                    return restTemplate.exchange(
                            userUrl,
                            HttpMethod.GET,
                            null,
                            Map.class
                    ).getBody();
        }
        return null;
    }
}
