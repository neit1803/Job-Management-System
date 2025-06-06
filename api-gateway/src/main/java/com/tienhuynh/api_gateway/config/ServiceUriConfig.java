package com.tienhuynh.api_gateway.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
public class ServiceUriConfig {
    @Value("${AUTH_SERVICE_URI}")
    private String authServiceUri;

}
