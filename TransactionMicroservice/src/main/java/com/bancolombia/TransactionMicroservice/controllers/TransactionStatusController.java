package com.bancolombia.TransactionMicroservice.controllers;



import com.bancolombia.TransactionMicroservice.services.TransactionStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/transactions")
public class TransactionStatusController {

    private final TransactionStatusService transactionStatusService;
    @Autowired
    public TransactionStatusController(TransactionStatusService transactionStatusService) {
        this.transactionStatusService = transactionStatusService;
    }

    @PostMapping("/transactionStatus")
    public Mono<Boolean> getTransactionStatus() {
        return transactionStatusService.approveTransaction();
    }
    @PostMapping("/transactionHistory/user/{identification}")
    public Flux<CashoutDTO> findByIdentification(@PathVariable("identification")  Long identification){
        return transactionStatusService.searchCashOutByIdentification(identification).map(cashout -> {
            var dto = new CashoutDTO();
            dto.setId(cashout.getId());
            dto.setIdentification(cashout.getIdentification());
            dto.setAmount(cashout.getAmount());
            return dto;
        });
    }
}
