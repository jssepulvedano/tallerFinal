package com.bancolombia.tallerFinal.services;

import com.bancolombia.tallerFinal.controllers.CashoutDTO;
import com.bancolombia.tallerFinal.domain.Cashout;
import com.bancolombia.tallerFinal.domain.User;
import com.bancolombia.tallerFinal.domain.repositories.CashoutRepository;
import com.bancolombia.tallerFinal.domain.repositories.UserRepository;
import com.bancolombia.tallerFinal.exceptions.InsuficientBalanceException;
import com.bancolombia.tallerFinal.exceptions.TransactionNotApprovedException;
import com.bancolombia.tallerFinal.exceptions.UserNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class CashoutServiceTest {
    @Mock
    private CashoutRepository cashoutRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WebClient webClient;

    @InjectMocks
    private CashoutService cashoutService;

    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    private WebClient.ResponseSpec responseSpec;
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;

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
    void createCashout_SuccessfulTransaction() {
        Long userIdentification = 123L;
        Cashout request = new Cashout();
        request.setIdentification(userIdentification);
        request.setAmount(100.0);

        User user = new User();
        user.setBalance(200.0);
        user.setIdentification(userIdentification);

        when(userRepository.findByIdentification(userIdentification)).thenReturn(Mono.just(user));
        when(cashoutRepository.save(any(Cashout.class))).thenReturn(Mono.just(request));
        when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));

        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(true));

        StepVerifier.create(cashoutService.createCashout(request))
                .expectNext(request)
                .verifyComplete();

        verify(userRepository, times(1)).findByIdentification(userIdentification);
        verify(cashoutRepository, times(1)).save(request);
    }

    @Test
    void createCashout_InsufficientBalance() {
        Long userIdentification = 123L;
        Cashout request = new Cashout();
        request.setIdentification(userIdentification);
        request.setAmount(300.0);

        User user = new User();
        user.setBalance(200.0);
        user.setIdentification(userIdentification);

        when(userRepository.findByIdentification(userIdentification)).thenReturn(Mono.just(user));

        StepVerifier.create(cashoutService.createCashout(request))
                .expectError(InsuficientBalanceException.class)
                .verify();

        verify(userRepository, times(1)).findByIdentification(userIdentification);
        verify(cashoutRepository, never()).save(any(Cashout.class));
    }

    @Test
    void createCashout_TransactionNotApproved() {
        Long userIdentification = 123L;
        Cashout request = new Cashout();
        request.setIdentification(userIdentification);
        request.setAmount(100.0);

        User user = new User();
        user.setBalance(200.0);
        user.setIdentification(userIdentification);

        when(userRepository.findByIdentification(userIdentification)).thenReturn(Mono.just(user));
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(false));

        StepVerifier.create(cashoutService.createCashout(request))
                .expectError(TransactionNotApprovedException.class)
                .verify();

        verify(userRepository, times(1)).findByIdentification(userIdentification);
        verify(cashoutRepository, never()).save(any(Cashout.class));
    }

}