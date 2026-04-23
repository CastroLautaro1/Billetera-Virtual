package com.billetera_virtual.deposit.infra.web;

import com.billetera_virtual.deposit.application.CreateDepositLinkUseCase;
import com.billetera_virtual.deposit.dto.DepositRequestDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @Operation(
            summary = "Generar link de pago para un deposito",
            description = "Crea una preferencia de cobro en Mercado Pago para que el usuario recargue saldo. Recibe el monto y el email del usuario, y retorna la URL a la cual debe ser redirigido para completar la transacción."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "URL con el link a Mercado Pago",
                    content = @Content(mediaType = "text/plain",
                            schema = @Schema(implementation = String.class))
            )
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/deposit")
    public ResponseEntity<String> createDeposit(@RequestBody DepositRequestDTO request){
        String paymentUrl = createDepositLinkUseCase.execute(request.amount(), request.userEmail());
        return ResponseEntity.ok(paymentUrl);
    }
}
