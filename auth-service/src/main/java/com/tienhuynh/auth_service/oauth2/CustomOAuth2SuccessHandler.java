package com.tienhuynh.auth_service.oauth2;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.tienhuynh.auth_service.payload.AuthRequest;
import com.tienhuynh.auth_service.payload.RegisterRequest;
import com.tienhuynh.auth_service.payload.UserPayload;
import com.tienhuynh.auth_service.rabbitmq.RabbitMQProducer;
import com.tienhuynh.auth_service.security.JwtUtil;
import com.tienhuynh.auth_service.service.AuthService;
import com.tienhuynh.auth_service.service.FacebookOAuthService;
import com.tienhuynh.auth_service.service.GoogleOAuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler{

    @Bean
    private GoogleOAuthService googleOAuthService() {
        return new GoogleOAuthService();
    }

    @Bean
    private final FacebookOAuthService facebookOAuthService() {
        return new FacebookOAuthService();
    }

    @Bean
    private RabbitMQProducer rabbitMQProducer() {
        return new RabbitMQProducer(new RabbitTemplate());
    }

    @Bean
    private AuthService authService () {
        return new AuthService(new JwtUtil());
    }

    @Bean
    private ObjectMapper jsonObjectMapper() {
        return new  JsonMapper();
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        List<String> callBackUri = List.of(request.getRequestURI().split("/"));
        String registrationId = callBackUri.get(callBackUri.size() - 1).toLowerCase().trim();

        RegisterRequest req = null;
        String errorMsg = "";
        switch (registrationId) {
            case "google":
                req = googleOAuthService().fetchUserInfo(oauthUser);
                break;
                case "facebook":
                    req = facebookOAuthService().fetchUserInfo(oauthUser);
                    break;
            default:
                errorMsg = "Unsupported Social";
                break;
        }

        if (req != null) {
            String resp = rabbitMQProducer().getUser(new AuthRequest(req.getMail(), req.getPwd_hash()));

            UserPayload user;
            try {
                user = jsonObjectMapper().readValue(resp, UserPayload.class);
            } catch (Exception e) {
                authService().register(req);
            }

            authService().login(new AuthRequest(req.getMail(), req.getPwd_hash()));
        }

        response.sendRedirect("http://localhost:3000/oauth2/success?msg=" + errorMsg);

        
        // Có thể cần mapping giữa userInfo từ từng provider về định dạng chung
        // Gửi thông tin này về AuthService để tạo JWT
//        String jwt = generateJwtToken(email); // bạn tự hiện thực hoặc gọi API

        // Redirect về frontend
//        response.sendRedirect("http://localhost:3000/oauth2/success?token=" + "&provider=" + registrationId);
    }
}
