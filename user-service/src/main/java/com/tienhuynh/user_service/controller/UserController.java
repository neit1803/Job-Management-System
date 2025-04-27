package com.tienhuynh.user_service.controller;

import com.tienhuynh.user_service.dto.UserDto;
import com.tienhuynh.user_service.model.User;
import com.tienhuynh.user_service.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserServiceImpl userService;

    @GetMapping("")
    public ResponseEntity<Iterable<UserDto>> users() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(userService.getUser(UUID.fromString(id)));
    }

    @PostMapping
    public ResponseEntity<UserDto> saveUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }

    @PatchMapping
    public ResponseEntity<UserDto> updateUser(@PathVariable String id, @RequestBody User user) {
        return ResponseEntity.ok(userService.update(UUID.fromString(id), user));
    }
}
