package com.bancolombia.TransactionMicroservice.services;

import com.bancolombia.TransactionMicroservice.domain.Cashout;
import com.bancolombia.TransactionMicroservice.domain.repositories.CashoutRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class TransactionStatusServiceTest {

    @Mock
    private CashoutRepository cashoutRepository;

    @InjectMocks
    private TransactionStatusService transactionStatusService;

    public TransactionStatusServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testApproveTransaction() {
        // Act
        Mono<Boolean> result = transactionStatusService.approveTransaction();

        // Assert
        // Since the approval is random, we can only assert that it returns a Mono<Boolean>
        assertTrue(result.block() instanceof Boolean);
    }

    @Test
    void testSearchCashOutByIdentification_WithResults() {
        // Arrange
        Long identification = 123L;
        Cashout cashout1 = new Cashout(); // Configura con datos necesarios
        cashout1.setId("1");
        cashout1.setIdentification(identification);
        cashout1.setAmount(100.0);

        Cashout cashout2 = new Cashout(); // Configura con datos necesarios
        cashout2.setId("2");
        cashout2.setIdentification(identification);
        cashout2.setAmount(200.0);

        when(cashoutRepository.findByIdentification(identification)).thenReturn(Flux.just(cashout1, cashout2));

        // Act
        Flux<Cashout> result = transactionStatusService.searchCashOutByIdentification(identification);

        // Assert
        assertTrue(result.hasElements().block());
        assertTrue(result.collectList().block().size() == 2);
    }

    @Test
    void testSearchCashOutByIdentification_EmptyResults() {
        // Arrange
        Long identification = 456L;

        when(cashoutRepository.findByIdentification(identification)).thenReturn(Flux.empty());

        // Act
        Flux<Cashout> result = transactionStatusService.searchCashOutByIdentification(identification);

        // Assert
        assertTrue(result.collectList().block().isEmpty());
    }
}