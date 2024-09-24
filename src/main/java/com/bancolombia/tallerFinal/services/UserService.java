package com.bancolombia.tallerFinal.services;

import com.bancolombia.tallerFinal.domain.User;
import com.bancolombia.tallerFinal.domain.repositories.UserRepository;
import reactor.core.publisher.Mono;

public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> createUser(User request) {
        return this.userRepository.save(request);
    }
}
