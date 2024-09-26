package com.bancolombia.tallerFinal.services;

import com.bancolombia.tallerFinal.domain.User;
import com.bancolombia.tallerFinal.domain.repositories.UserRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> createUser(User request) {
        return this.userRepository.save(request);
    }

    public Mono<User> searchUserByIdentification(Long identification) {
        return this.userRepository.findByIdentification(identification);
    }

    public Mono<User> updateUserBalanceByIdentification(Long identification, Double balanceToAdd) {
        return userRepository.findByIdentification(identification)
                .flatMap(user -> {
                    // Sumar el balance nuevo al actual
                    double updatedBalance = user.getBalance() + balanceToAdd;
                    user.setBalance(updatedBalance);
                    // Guardar el usuario actualizado
                    return userRepository.save(user);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
    }
}
