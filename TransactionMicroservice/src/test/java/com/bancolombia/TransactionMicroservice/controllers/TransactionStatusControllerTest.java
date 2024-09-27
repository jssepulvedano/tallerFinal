package com.bancolombia.TransactionMicroservice.controllers;

import com.bancolombia.TransactionMicroservice.domain.Cashout;
import com.bancolombia.TransactionMicroservice.services.TransactionStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionStatusControllerTest {
    @Mock
    private TransactionStatusService transactionStatusService;

    @InjectMocks
    private TransactionStatusController transactionStatusController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(transactionStatusController).build();
    }

    @Test
    void testGetTransactionStatus() {
        when(transactionStatusService.approveTransaction()).thenReturn(Mono.just(true));

        webTestClient.post()
                .uri("/transactions/transactionStatus")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Boolean.class)
                .isEqualTo(true);

        verify(transactionStatusService, times(1)).approveTransaction();
    }

    @Test
    void testFindByIdentification() {
        Long identification = 123L;

        // Crear objetos Cashout para la prueba
        Cashout cashout1 = new Cashout();
        cashout1.setId("1");
        cashout1.setIdentification(identification);
        cashout1.setAmount(100.0);

        Cashout cashout2 = new Cashout();
        cashout2.setId("2");
        cashout2.setIdentification(identification);
        cashout2.setAmount(200.0);


        when(transactionStatusService.searchCashOutByIdentification(identification))
                .thenReturn(Flux.just(cashout1, cashout2));

        webTestClient.post()
                .uri("/transactions/transactionHistory/user/" + identification)
                .exchange()
                .expectStatus().isOk()
                .returnResult(Cashout.class)
                .getResponseBody()
                .collectList()
                .block()
                .forEach(cashout -> {
                    assertTrue(cashout.equals(cashout1) || cashout.equals(cashout2));
                });

        verify(transactionStatusService, times(1)).searchCashOutByIdentification(identification);
    }
}