package com.tienhuynh.user_service.controller;

import com.tienhuynh.user_service.model.User;
import com.tienhuynh.user_service.service.UserServiceImpl;
import org.antlr.v4.runtime.misc.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;

@Controller
@RequestMapping("/api/v1/user")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/users")
    public @NotNull ResponseEntity<Iterable<User>> users() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUser(UUID.fromString(id)));
    }
}
