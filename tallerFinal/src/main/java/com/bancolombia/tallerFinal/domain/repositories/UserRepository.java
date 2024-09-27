package com.bancolombia.tallerFinal.domain.repositories;

import com.bancolombia.tallerFinal.domain.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User,String> {
    Mono<User> findByIdentification(Long identification);

}
