package com.billetera_virtual.transaction.infra.web;

import com.billetera_virtual.security.infra.model.UserPrincipal;
import com.billetera_virtual.transaction.domain.Transaction;
import com.billetera_virtual.transaction.domain.port.TransactionServicePort;
import com.billetera_virtual.transaction.infra.web.dto.TransactionDTO;
import com.billetera_virtual.transaction.infra.web.mapper.TransactionMapperWeb;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.time.OffsetDateTime;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionServicePort transactionService;
    private final TransactionMapperWeb transactionMapper;

    @Operation(
            summary = "Realizar una transferencia",
            description = "El usuario realiza una transferencia a otra cuenta, mediante un Alias o CVU. El dinero utilizado será el de la propia cuenta del usuario logueado y la transacción quedará registrada."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transacción exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Transaction.class)))
    })
    @PreAuthorize("hasRole('USER')")
    @PostMapping("/transfer")
    public ResponseEntity<Transaction> makeTransfer(
            @Valid @RequestBody TransactionDTO dto,
            @AuthenticationPrincipal UserPrincipal principal)
    {
        Long userId = principal.getId();
        Transaction transaction = transactionMapper.toDomain(dto);
        Transaction saved = transactionService.makeTransaction(transaction, dto.destination(), userId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }

    @Operation(
            summary = "Obtener una reseña por su Id (Solo Admin)",
            description = "El Admin ingresa el Id de una transferencia y obtiene información de la misma. "
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transaccón existente obtenida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron transacciones con ese ID")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getById(@PathVariable Long id) {
        Transaction transaction = transactionService.getById(id);
        return ResponseEntity.ok(transaction);
    }

    @Operation(
            summary = "Obtener todas las transacciones del usuario logueado",
            description = "El usuario logueado obtiene un historial de todas las transacciones donde figure su Id de cuenta ya sea como origen o contraparte. El usuario puede aplicar los siguiente filtros al historial: tipo, monto mínimo, monto máximo, y un rango de fechas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Transacción exitosa",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron usuarios con ese ID")
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/history")
    public ResponseEntity<Page<Transaction>> getTransactionHistory(
            @AuthenticationPrincipal UserPrincipal principal,
            @RequestParam(required = false) Transaction.TransactionType type,
            @RequestParam(required = false) Double minAmount,
            @RequestParam(required = false) Double maxAmount,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime end,
            @PageableDefault(size = 10, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Long accountId = principal.getAccountId();
        Page<Transaction> transactions = transactionService.getHistory(accountId, type, minAmount, maxAmount,
                start, end, pageable);
        return ResponseEntity.ok(transactions);
    }

}
