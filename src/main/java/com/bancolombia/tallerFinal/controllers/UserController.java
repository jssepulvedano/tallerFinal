package com.bancolombia.tallerFinal.controllers;

import com.bancolombia.tallerFinal.domain.User;
import com.bancolombia.tallerFinal.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping
    public Mono<User> createUser(@RequestBody User request) {
        return userService.createUser(request);
    }
}
