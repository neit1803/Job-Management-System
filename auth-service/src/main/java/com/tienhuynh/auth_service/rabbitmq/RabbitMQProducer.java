package com.tienhuynh.auth_service.rabbitmq;

import com.tienhuynh.auth_service.model.RegisterRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RabbitMQProducer   {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public void sendCheckUserRequest(RegisterRequest req) {
        rabbitTemplate.convertAndSend("user.exchange", "user.check.request", req);
    }

    public void sendCreateUserRequest(RegisterRequest req) {
        rabbitTemplate.convertAndSend("user.exchange", "user.save.request", req);
    }
}
