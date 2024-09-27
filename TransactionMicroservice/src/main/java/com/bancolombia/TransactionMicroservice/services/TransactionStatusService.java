package com.bancolombia.TransactionMicroservice.services;

import com.bancolombia.TransactionMicroservice.domain.Cashout;
import com.bancolombia.TransactionMicroservice.domain.repositories.CashoutRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Random;

@Service
public class TransactionStatusService {
    private final CashoutRepository cashoutRepository;

    public TransactionStatusService(CashoutRepository cashoutRepository) {
        this.cashoutRepository = cashoutRepository;
    }

    public Mono<Boolean> approveTransaction() {
        boolean isApproved = new Random().nextBoolean();
        return Mono.just(isApproved);
    }

    public Flux<Cashout> searchCashOutByIdentification(Long identification) {
        return cashoutRepository.findByIdentification(identification);
    }
}
