package com.bancolombia.tallerFinal.controllers;

import com.bancolombia.tallerFinal.domain.Cashout;


import com.bancolombia.tallerFinal.services.CashoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/cashouts")
public class CashoutController {
    @Autowired
    private CashoutService cashoutService;
    @PostMapping
    public Mono<Cashout> createCashout(@RequestBody Cashout request) {
        return cashoutService.createCashout(request);
    }
}
