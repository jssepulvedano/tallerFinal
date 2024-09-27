package com.bancolombia.tallerFinal.services;

import com.bancolombia.tallerFinal.controllers.CashoutDTO;
import com.bancolombia.tallerFinal.controllers.UserDTO;
import com.bancolombia.tallerFinal.domain.Cashout;
import com.bancolombia.tallerFinal.domain.repositories.CashoutRepository;
import com.bancolombia.tallerFinal.domain.repositories.UserRepository;
import com.bancolombia.tallerFinal.exceptions.InsuficientBalanceException;
import com.bancolombia.tallerFinal.exceptions.TransactionNotApprovedException;
import com.bancolombia.tallerFinal.exceptions.UserNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.function.Function;
@Service
public class CashoutService {
    private final CashoutRepository cashoutRepository;
    private final WebClient webClient;
    private final UserRepository userRepository;

    public CashoutService(CashoutRepository cashoutRepository, WebClient webClient, UserRepository userRepository) {
        this.cashoutRepository = cashoutRepository;
        this.webClient = webClient;
        this.userRepository = userRepository;
    }

    public Mono<Cashout> createCashout(Cashout request) {
        return userRepository.findByIdentification(request.getIdentification())
                .onErrorMap(throwable -> new UserNotFoundException("User not found"))
                .doOnNext(user -> System.out.println("Checking Balance"))
                .flatMap(user -> {


                    if (user.getBalance() <= request.getAmount()){
                        return Mono.error(new InsuficientBalanceException("Not enough Money in the Balance "));
                    } else {
                        return checkTransactionStatus().flatMap(isApproved -> {
                            if (isApproved) {
                                double updatedBalance = user.getBalance() - request.getAmount();
                                user.setBalance(updatedBalance);

                                return userRepository.save(user).
                                        doOnSuccess(savedUser -> System.out.println("Balance Update"))
                                        .thenReturn(request);
                            }else {
                                return Mono.error(new TransactionNotApprovedException("Transaction not approved"));
                            }
                        });
                    }
                })
                .doOnNext(user -> System.out.println("Balance updated, Checking Balance"))
                .flatMap(cashoutSave -> cashoutRepository.save(request));
    }

    private Mono<Boolean> checkTransactionStatus() {
        return webClient.post()
                .uri("/transactionStatus")
                .retrieve()
                .bodyToMono(Boolean.class);
    }

    public Flux<CashoutDTO> searchCashOutByIdentification(Long identification) {
        return getTransactionHistory(identification);
    }

    private Flux<CashoutDTO> getTransactionHistory(Long identification) {
        return webClient.post()
                .uri("/transactionHistory/user/"+identification.toString())
                .retrieve()
                .bodyToFlux(CashoutDTO.class);
    }
}
