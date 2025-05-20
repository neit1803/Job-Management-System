package com.tienhuynh.user_service.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienhuynh.user_service.model.User;
import com.tienhuynh.user_service.service.UserServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    private ObjectMapper jsonObjectMapper;

    @RabbitListener(queues = "user.save.request.queue")
    public String handleRegisterRequest(String msg) {
        return msg;
    }

    @RabbitListener(queues = "user.get.request.queue")
    public String handleGetUserRequest(String msg) {
        User user = jsonObjectMapper.convertValue(msg, User.class);
        User resp = userService.getUserByMail(user.getMail());
        return jsonObjectMapper.convertValue(resp, String.class);
    }
}
