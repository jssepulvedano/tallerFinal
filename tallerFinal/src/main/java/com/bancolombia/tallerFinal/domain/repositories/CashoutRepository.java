package com.bancolombia.tallerFinal.domain.repositories;

import com.bancolombia.tallerFinal.domain.Cashout;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CashoutRepository extends ReactiveCrudRepository<Cashout,String> {
}
