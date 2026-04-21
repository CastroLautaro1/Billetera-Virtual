package com.billetera_virtual.deposit.infra.web;

import com.billetera_virtual.deposit.application.WebhookDepositUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final WebhookDepositUseCase webhookDepositUseCase;

    public WebhookController(WebhookDepositUseCase webhookDepositUseCase) {
        this.webhookDepositUseCase = webhookDepositUseCase;
    }

    @Operation(
            summary = "Webhook de Mercado Pago (Notificaciones)",
            description = "Endpoint público consumido automáticamente por los servidores de Mercado Pago para avisar cambios en el estado de un pago. Si el pago es aprobado, valida la idempotencia, actualiza el saldo del usuario y registra la transacción."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Notificación recibida y procesada correctamente (siempre devuelve 200 para que MP no reintente)",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(implementation = String.class))
            )
    })
    @PostMapping("/mercadopago")
    public ResponseEntity<String> receiveNotification(
            @RequestParam(name = "data.id", required = false) String paymentId,
            @RequestParam(name = "type", required = false) String type) {

        if ("payment".equals(type) && paymentId != null) {
            webhookDepositUseCase.processWebHookNotification(paymentId);
        }

        return ResponseEntity.ok("Notificación recibida");
    }
}
