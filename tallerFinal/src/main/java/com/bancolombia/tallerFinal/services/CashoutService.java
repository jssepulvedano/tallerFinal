package com.bancolombia.tallerFinal.services;

import com.bancolombia.tallerFinal.domain.Cashout;
import com.bancolombia.tallerFinal.domain.repositories.CashoutRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@Service
public class CashoutService {
    private final CashoutRepository cashoutRepository;

    public CashoutService(CashoutRepository cashoutRepository) {
        this.cashoutRepository = cashoutRepository;
    }

    public Mono<Cashout> createCashout(Cashout request) {
        return cashoutRepository.save(request);
    }
}
