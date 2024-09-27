package com.bancolombia.TransactionMicroservice.domain.repositories;

import com.bancolombia.TransactionMicroservice.domain.Cashout;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CashoutRepository extends ReactiveCrudRepository<Cashout,String> {
    Flux<Cashout> findByIdentification(Long identification);
}
