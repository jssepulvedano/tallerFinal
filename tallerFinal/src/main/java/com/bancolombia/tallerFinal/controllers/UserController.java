package com.bancolombia.tallerFinal.controllers;

import com.bancolombia.tallerFinal.domain.BalanceRequest;
import com.bancolombia.tallerFinal.domain.User;
import com.bancolombia.tallerFinal.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/create")
    public Mono<User> createUser(@RequestBody User request) {
        return userService.createUser(request);
    }
    @GetMapping("/{identification}")
    public Mono<UserDTO> findByIdentification(@PathVariable("identification") Long identification){
        return userService.searchUserByIdentification(identification).map(user -> {
            var dto = new UserDTO();
            dto.setIdentification(user.getIdentification());
            dto.setName(user.getName());
            dto.setBalance(user.getBalance());
            return dto;
        });
    }
    @PostMapping("/{identification}/balance")
    public Mono<User>  updateUserBalance(@PathVariable("identification") Long identification, @RequestBody BalanceRequest request){
        return userService.updateUserBalanceByIdentification(identification, request.getBalance());
    }
}
