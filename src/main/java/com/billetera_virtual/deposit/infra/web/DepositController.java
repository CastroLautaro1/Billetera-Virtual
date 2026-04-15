package com.billetera_virtual.deposit.infra.web;

import com.billetera_virtual.deposit.application.CreateDepositLinkUseCase;
import com.billetera_virtual.deposit.dto.DepositRequestDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class DepositController {

    private final CreateDepositLinkUseCase createDepositLinkUseCase;

    public DepositController(CreateDepositLinkUseCase createDepositLinkUseCase) {
        this.createDepositLinkUseCase = createDepositLinkUseCase;
    }

    @PostMapping("/deposit")
    public ResponseEntity<String> createDeposit(@RequestBody DepositRequestDTO request){
        String paymentUrl = createDepositLinkUseCase.execute(request.amount(), request.userEmail());
        return ResponseEntity.ok(paymentUrl); //URL con el link a Mercado Pago
    }
}
