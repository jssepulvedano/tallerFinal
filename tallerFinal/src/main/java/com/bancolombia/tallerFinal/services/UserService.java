package com.bancolombia.tallerFinal.services;

import com.bancolombia.tallerFinal.domain.User;
import com.bancolombia.tallerFinal.domain.repositories.UserRepository;
import com.bancolombia.tallerFinal.exceptions.TransactionNotApprovedException;
import com.bancolombia.tallerFinal.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@Service
public class UserService {
    private final UserRepository userRepository;
    private final WebClient webClient;

    public UserService(UserRepository userRepository, WebClient webClient) {
        this.userRepository = userRepository;
        this.webClient = webClient;
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
                    return checkTransactionStatus().flatMap(isApproved -> {
                        if (isApproved) {
                            // Si la transacción es aprobada, actualiza el saldo
                            double updatedBalance = user.getBalance() + balanceToAdd;
                            user.setBalance(updatedBalance);
                            return userRepository.save(user);
                        } else {
                            // Si la transacción es rechazada, devuelve un error
                            return Mono.error(new TransactionNotApprovedException("Transaction not approved for user with identification: " + identification));
                        }
                    });
                })
                .switchIfEmpty(Mono.error(new UserNotFoundException("User not found")));
    }
    private Mono<Boolean> checkTransactionStatus() {
        return webClient.post()
                .uri("/transactionStatus")
                .retrieve()
                .bodyToMono(Boolean.class);
    }
}
