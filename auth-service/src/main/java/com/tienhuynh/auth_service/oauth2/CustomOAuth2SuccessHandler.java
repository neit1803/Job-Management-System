package com.tienhuynh.auth_service.oauth2;

import com.tienhuynh.auth_service.payload.RegisterRequest;
import com.tienhuynh.auth_service.service.FacebookOAuthService;
import com.tienhuynh.auth_service.service.GoogleOAuthService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler{

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
        switch (registrationId) {
            case "google":
                req = googleOAuthService().fetchUserInfo(oauthUser);
                break;
                case "facebook":
                    req = facebookOAuthService().fetchUserInfo(oauthUser);
                    break;
            default:
                response.sendRedirect("http://localhost:3000/oauth2/success?msg=" + "Unsupported Social");
                break;
        }
        System.out.println(req.toString());

        
        // Có thể cần mapping giữa userInfo từ từng provider về định dạng chung
        // Gửi thông tin này về AuthService để tạo JWT
//        String jwt = generateJwtToken(email); // bạn tự hiện thực hoặc gọi API

        // Redirect về frontend
//        response.sendRedirect("http://localhost:3000/oauth2/success?token=" + "&provider=" + registrationId);
    }

    @Bean
    private GoogleOAuthService googleOAuthService() {
        return new GoogleOAuthService();
    }

    @Bean
    private FacebookOAuthService facebookOAuthService() {
        return new FacebookOAuthService();
    }
}
