package com.bancolombia.tallerFinal.services;

import com.bancolombia.tallerFinal.domain.User;
import com.bancolombia.tallerFinal.domain.repositories.UserRepository;
import com.bancolombia.tallerFinal.exceptions.TransactionNotApprovedException;
import com.bancolombia.tallerFinal.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private WebClient webClient;

    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    private WebClient.ResponseSpec responseSpec;
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        requestHeadersUriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        requestHeadersSpec = mock(WebClient.RequestHeadersSpec.class);
        responseSpec = mock(WebClient.ResponseSpec.class);
        requestBodyUriSpec = mock(WebClient.RequestBodyUriSpec.class);

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);  
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(true));
    }

    @Test
    void createUser_Success() {
        User user = new User();
        user.setIdentification(123L);
        user.setBalance(100.0);

        when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));

        StepVerifier.create(userService.createUser(user))
                .expectNext(user)
                .verifyComplete();

        verify(userRepository, times(1)).save(user);
    }

    @Test
    void searchUserByIdentification_UserFound() {
        Long identification = 123L;
        User user = new User();
        user.setIdentification(identification);
        user.setBalance(200.0);

        when(userRepository.findByIdentification(identification)).thenReturn(Mono.just(user));

        StepVerifier.create(userService.searchUserByIdentification(identification))
                .expectNext(user)
                .verifyComplete();

        verify(userRepository, times(1)).findByIdentification(identification);
    }


    @Test
    void updateUserBalanceByIdentification_Success() {
        Long identification = 123L;
        Double balanceToAdd = 50.0;

        User user = new User();
        user.setIdentification(identification);
        user.setBalance(100.0);

        when(userRepository.findByIdentification(identification)).thenReturn(Mono.just(user));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(true));

        StepVerifier.create(userService.updateUserBalanceByIdentification(identification, balanceToAdd))
                .expectNext(user)
                .verifyComplete();

        verify(userRepository, times(1)).findByIdentification(identification);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void updateUserBalanceByIdentification_TransactionNotApproved() {
        Long identification = 123L;
        Double balanceToAdd = 50.0;

        User user = new User();
        user.setIdentification(identification);
        user.setBalance(100.0);

        when(userRepository.findByIdentification(identification)).thenReturn(Mono.just(user));
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(false));

        StepVerifier.create(userService.updateUserBalanceByIdentification(identification, balanceToAdd))
                .expectError(TransactionNotApprovedException.class)
                .verify();

        verify(userRepository, times(1)).findByIdentification(identification);
        verify(userRepository, never()).save(any(User.class));
    }

}