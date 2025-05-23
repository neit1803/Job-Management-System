package com.tienhuynh.auth_service.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienhuynh.auth_service.model.AuthRequest;
import com.tienhuynh.auth_service.model.RegisterRequest;
import com.tienhuynh.auth_service.model.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RabbitMQProducer   {
    private final RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper jsonObjectMapper;

    @Autowired
    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public String saveUser(RegisterRequest req) {
        try {
            String json = jsonObjectMapper.writeValueAsString(req);
            return (String) rabbitTemplate.convertSendAndReceive(
                    RabbitMQConfig.USER_EXCHANGE,
                    RabbitMQConfig.USER_SAVE_ROUTING_KEY,
                    json
            );
        } catch (JsonProcessingException e) {
            return e.getMessage();
        }
    }

    public String getUser(AuthRequest req) {
        try {
            String json = jsonObjectMapper.writeValueAsString(req);
            return (String) rabbitTemplate.convertSendAndReceive(
                    RabbitMQConfig.USER_EXCHANGE,
                    RabbitMQConfig.USER_GET_ROUTING_KEY,
                    json
            );
        } catch (JsonProcessingException e) {
            log.error("Error serializing AuthRequest object", e);
            return e.getMessage();
        }
    }
}
