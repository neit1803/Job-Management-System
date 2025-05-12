package com.tienhuynh.auth_service.rabbitmq;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {
    @Autowired
    private RabbitMQProducer authProducer;

    private PasswordEncoder passwordEncoder;

    public RabbitMQConsumer(RabbitMQProducer authProducer, PasswordEncoder passwordEncoder) {
        this.authProducer = authProducer;
        this.passwordEncoder = passwordEncoder;
    }

    @RabbitListener(queues = "auth.check.response.queue")
    public void handleCheckUserResponse(UserCheckResponse response) {
        if (!response.exists()) {
            // Hash password
            UserRegisterDTO dto = response.getOriginalDTO();
            dto.setPassword(passwordEncoder.encode(dto.getPassword()));

            // Gửi yêu cầu lưu user
            authProducer.sendCreateUserRequest(dto);
        } else {
            // Username/email đã tồn tại → xử lý lỗi
        }
    }
}
