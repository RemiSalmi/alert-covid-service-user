package com.polytech.alertcovidserviceuser.controllers;

import com.polytech.alertcovidserviceuser.models.User;
import com.polytech.alertcovidserviceuser.models.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users/")
public class UsersController {
    @Autowired
    private UserRepository userRepository;


    @GetMapping
    public List<User> list() {
        return userRepository.findAll();
    }

}
