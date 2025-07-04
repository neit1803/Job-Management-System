package com.tienhuynh.user_service.rabbitmq;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tienhuynh.user_service.dto.UserDto;
import com.tienhuynh.user_service.enums.Role;
import com.tienhuynh.user_service.model.CandidateProfile;
import com.tienhuynh.user_service.model.RecruiterProfile;
import com.tienhuynh.user_service.model.RegisterRequest;
import com.tienhuynh.user_service.model.User;
import com.tienhuynh.user_service.payload.CommonResponse;
import com.tienhuynh.user_service.service.UserServiceImpl;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class    RabbitMQConsumer {
    @Autowired
    UserServiceImpl userService;

    @Autowired
    private ObjectMapper jsonObjectMapper;

    @RabbitListener(queues = "user.save.request.queue")
    public String handleRegisterRequest(String msg) {
        try {
            RegisterRequest request = jsonObjectMapper.readValue(msg, RegisterRequest.class);

            User user = jsonObjectMapper.convertValue(request, User.class);
            user.setRole(Role.valueOf(request.getRole().toUpperCase()));

            if (user.getRole() == Role.RECRUITER) {
                RecruiterProfile profile = new RecruiterProfile();
                if (!request.getProfile().isEmpty()) {
                    profile = jsonObjectMapper.convertValue(request.getProfile(), RecruiterProfile.class);
                }
                profile.setUser(user);
                user.setRecruiterProfile(profile);
            } else if (user.getRole() == Role.CANDIDATE) {
                CandidateProfile profile = new CandidateProfile();
                if (!request.getProfile().isEmpty()) {
                    profile = jsonObjectMapper.convertValue(request.getProfile(), CandidateProfile.class);
                }
                profile.setUser(user);
                user.setCandidateProfile(profile);
            }
            userService.save(user);
            return "SUCCESS";

        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR: " + e.getMessage();
        }
    }


    @RabbitListener(queues = "user.get.request.queue")
    public String handleGetUserRequest(String mail){
        try {
            User found = userService.getUserByMail(mail);
            return jsonObjectMapper.writeValueAsString(found);
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }

    @RabbitListener(queues = "user.update.request.queue")
    public String handleUpdateUserRequest(String msg) {
        try {
            System.out.println(msg);
            return "jsonObjectMapper.writeValueAsString(found)";
        } catch (Exception e) {
            return "ERROR: " + e.getMessage();
        }
    }
}
