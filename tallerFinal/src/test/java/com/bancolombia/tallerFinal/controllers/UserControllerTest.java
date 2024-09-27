package com.bancolombia.tallerFinal.controllers;

import com.bancolombia.tallerFinal.domain.BalanceRequest;
import com.bancolombia.tallerFinal.domain.User;
import com.bancolombia.tallerFinal.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private WebTestClient webTestClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        webTestClient = WebTestClient.bindToController(userController).build();
    }

    @Test
    void testCreateUser() {
        User userRequest = new User();
        userRequest.setId("1");
        userRequest.setIdentification(123L);
        userRequest.setName("John Doe");
        userRequest.setBalance(500.0);

        User userResponse = new User();
        userResponse.setId("1");
        userResponse.setIdentification(123L);
        userResponse.setName("John Doe");
        userResponse.setBalance(500.0);

        when(userService.createUser(any(User.class))).thenReturn(Mono.just(userResponse));

        webTestClient.post()
                .uri("/users/create")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(userRequest)
                .exchange()
                .expectStatus().isOk()
                .returnResult(User.class)
                .getResponseBody()
                .single()
                .doOnNext(result -> {
                    assertEquals(userResponse.getId(), result.getId());
                    assertEquals(userResponse.getIdentification(), result.getIdentification());
                    assertEquals(userResponse.getName(), result.getName());
                    assertEquals(userResponse.getBalance(), result.getBalance());
                })
                .block();

        verify(userService, times(1)).createUser(any(User.class));
    }

    @Test
    void testFindByIdentification() {
        Long identification = 123L;

        User user = new User();
        user.setId("1");
        user.setIdentification(identification);
        user.setName("John Doe");
        user.setBalance(500.0);

        UserDTO userDTO = new UserDTO();
        userDTO.setIdentification(identification);
        userDTO.setName("John Doe");
        userDTO.setBalance(500.0);

        when(userService.searchUserByIdentification(identification)).thenReturn(Mono.just(user));

        webTestClient.get()
                .uri("/users/" + identification)
                .exchange()
                .expectStatus().isOk()
                .returnResult(UserDTO.class)
                .getResponseBody()
                .single()
                .doOnNext(result -> {

                    assertEquals(userDTO.getId(), result.getId());
                    assertEquals(userDTO.getIdentification(), result.getIdentification());
                    assertEquals(userDTO.getName(), result.getName());
                    assertEquals(userDTO.getBalance(), result.getBalance());
                })
                .block();

        verify(userService, times(1)).searchUserByIdentification(identification);
    }

    @Test
    void testUpdateUserBalance() {
        Long identification = 123L;
        BalanceRequest balanceRequest = new BalanceRequest();
        balanceRequest.setBalance(100.0);

        User user = new User();
        user.setId("1");
        user.setIdentification(identification);
        user.setName("John Doe");
        user.setBalance(600.0);

        when(userService.updateUserBalanceByIdentification(eq(identification), any(Double.class)))
                .thenReturn(Mono.just(user));

        webTestClient.post()
                .uri("/users/" + identification + "/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(balanceRequest)
                .exchange()
                .expectStatus().isOk()
                .returnResult(User.class)
                .getResponseBody()
                .single()
                .doOnNext(result -> {

                    assertEquals(user.getId(), result.getId());
                    assertEquals(user.getIdentification(), result.getIdentification());
                    assertEquals(user.getName(), result.getName());
                    assertEquals(user.getBalance(), result.getBalance());
                })
                .block();

        verify(userService, times(1)).updateUserBalanceByIdentification(eq(identification), any(Double.class));
    }
}