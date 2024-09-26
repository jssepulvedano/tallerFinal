package com.bancolombia.tallerFinal.domain.repositories;

import com.bancolombia.tallerFinal.domain.Cashout;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CashoutRepository extends ReactiveCrudRepository<Cashout,String> {
}
