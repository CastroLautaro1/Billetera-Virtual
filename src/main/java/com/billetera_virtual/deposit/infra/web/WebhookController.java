package com.billetera_virtual.deposit.infra.web;

import com.billetera_virtual.deposit.application.WebhookDepositUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final WebhookDepositUseCase webhookDepositUseCase;

    public WebhookController(WebhookDepositUseCase webhookDepositUseCase) {
        this.webhookDepositUseCase = webhookDepositUseCase;
    }

    @PostMapping("/mercadopago")
    public ResponseEntity<String> receiveNotification(
            @RequestParam(name = "data.id", required = false) String paymentId,
            @RequestParam(name = "type", required = false) String type) {

        if ("payment".equals(type) && paymentId != null) {
            webhookDepositUseCase.processWebHookNotification(paymentId);
        }

        return ResponseEntity.ok("Notificación recibida"); // Devolver status 200 a MP
    }
}
