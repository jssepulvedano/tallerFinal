package com.bancolombia.tallerFinal.controllers;

import com.bancolombia.tallerFinal.controllers.CashoutController;
import com.bancolombia.tallerFinal.domain.Cashout;
import com.bancolombia.tallerFinal.services.CashoutService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CashoutControllerTest {
    @Mock
    private CashoutService cashoutService;

    @InjectMocks
    private CashoutController cashoutController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(cashoutController).build();
    }

    @Test
    void testCreateCashout() {
        Cashout cashoutRequest = new Cashout();
        cashoutRequest.setId("1");
        cashoutRequest.setIdentification(123L);
        cashoutRequest.setAmount(100.0);

        Cashout cashoutResponse = new Cashout();
        cashoutResponse.setId("1");
        cashoutResponse.setIdentification(123L);
        cashoutResponse.setAmount(100.0);

        when(cashoutService.createCashout(any(Cashout.class))).thenReturn(Mono.just(cashoutResponse));

        webTestClient.post()
                .uri("/cashouts")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(cashoutRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Cashout.class)
                .consumeWith(response -> {
                    Cashout body = response.getResponseBody();
                    assertNotNull(body);


                    assertEquals(cashoutResponse.getId(), body.getId(), "Response body does not contain the expected Cashout ID");
                    assertEquals(cashoutResponse.getIdentification(), body.getIdentification(), "Response body does not contain the expected identification");
                    assertEquals(cashoutResponse.getAmount(), body.getAmount(), "Response body does not contain the expected amount");
                });

        verify(cashoutService, times(1)).createCashout(any(Cashout.class));
    }

    @Test
    void testFindByIdentification() {
        Long identification = 123L;

        CashoutDTO cashoutDTO1 = new CashoutDTO();
        cashoutDTO1.setId("1");
        cashoutDTO1.setIdentification(123L);
        cashoutDTO1.setAmount(100.0);

        CashoutDTO cashoutDTO2 = new CashoutDTO();
        cashoutDTO2.setId("2");
        cashoutDTO2.setIdentification(123L);
        cashoutDTO2.setAmount(200.0);

        when(cashoutService.searchCashOutByIdentification(identification)).thenReturn(Flux.just(cashoutDTO1, cashoutDTO2));

        webTestClient.get()
                .uri("/cashouts/user/" + identification)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CashoutDTO.class)
                .consumeWith(response -> {
                    List<CashoutDTO> body = response.getResponseBody();
                    assertNotNull(body);

                    // Verificamos que el cuerpo contenga los IDs esperados
                    assertTrue(body.stream().anyMatch(dto -> dto.getId().equals(cashoutDTO1.getId())),
                            "Response body does not contain cashoutDTO1");
                    assertTrue(body.stream().anyMatch(dto -> dto.getId().equals(cashoutDTO2.getId())),
                            "Response body does not contain cashoutDTO2");
                });

        verify(cashoutService, times(1)).searchCashOutByIdentification(identification);
    }
}