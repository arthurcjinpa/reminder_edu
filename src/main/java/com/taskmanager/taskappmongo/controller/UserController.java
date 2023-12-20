package com.taskmanager.taskappmongo.controller;

import com.taskmanager.taskappmongo.entity.UserProfile;
import com.taskmanager.taskappmongo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public List<UserProfile> getAllUsers() {
        return userService.getAllUsers();
    }
}
