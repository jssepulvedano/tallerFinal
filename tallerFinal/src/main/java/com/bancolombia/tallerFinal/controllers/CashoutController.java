package com.bancolombia.tallerFinal.controllers;

import com.bancolombia.tallerFinal.domain.Cashout;


import com.bancolombia.tallerFinal.services.CashoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/cashouts")
public class CashoutController {
    private final CashoutService cashoutService;

    @Autowired
    public CashoutController(CashoutService cashoutService) {
        this.cashoutService = cashoutService;
    }
    @PostMapping
    public Mono<Cashout> createCashout(@RequestBody Cashout request) {
        return cashoutService.createCashout(request);
    }
    @GetMapping("/user/{identification}")
    public Flux<CashoutDTO> findByIdentification(@PathVariable("identification") Long identification){
        return cashoutService.searchCashOutByIdentification(identification);
    }

}
