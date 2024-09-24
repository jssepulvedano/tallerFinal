package com.bancolombia.tallerFinal.domain.repositories;

import com.bancolombia.tallerFinal.domain.User;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UserRepository extends ReactiveCrudRepository<User,String> {
}
