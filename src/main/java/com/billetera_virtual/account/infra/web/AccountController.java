package com.billetera_virtual.account.infra.web;

import com.billetera_virtual.account.domain.Account;
import com.billetera_virtual.account.domain.dto.AccountPublicDataResponse;
import com.billetera_virtual.account.domain.port.AccountServicePort;
import com.billetera_virtual.account.infra.web.dto.AccountRequest;
import com.billetera_virtual.account.infra.web.dto.AliasUpdateRequest;
import com.billetera_virtual.account.infra.web.mapper.AccountMapperWeb;
import com.billetera_virtual.security.infra.model.UserPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {

    private final AccountServicePort accountService;
    private final AccountMapperWeb accountMapperWeb;

    @Operation(
            summary = "Crear nueva cuenta bancaria (Solo Admin)",
            description = "Registra una nueva cuenta asociada a un usuario existente. Genera automáticamente el CVU y un alias único si no se proporcionan. Requiere privilegios de ADMINISTRADOR."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cuenta creada exitosamente.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/create")
    public ResponseEntity<Account> createAccount(@RequestBody AccountRequest request) {
        Account accountDomain = accountMapperWeb.toDomain(request);
        Account account = accountService.createAccount(accountDomain);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(account.getId())
                .toUri();
        return ResponseEntity.created(location).body(account);
    }

    @Operation(
            summary = "Obtener cuenta por Id (Solo Admin)",
            description = "Se obtiene una cuenta ingresando su Id."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta existente obtenida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron cuentas con ese ID")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("admin/{id}")
    public ResponseEntity<Account> getById(@PathVariable Long id) {
        Account account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @Operation(
            summary = "Obtener todas las cuentas (Solo Admin)",
            description = "Se obtienen todas las cuentas del sistema, acitvas e inactivas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Todas las cuentas obtenidas",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class)))
    })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("admin/all")
    public ResponseEntity<List<Account>> getAll() {
        List<Account> account = accountService.getAll();
        return ResponseEntity.ok(account);
    }

    @Operation(
            summary = "Obtener datos de la cuenta del usuario logueado",
            description = "El usuario que este logueado obtiene los datos de su cuenta (Id, Balance, Alias, CVU)."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cuenta del usuario",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Account.class)))
    })
    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    public ResponseEntity<Account> getByAccountId(@AuthenticationPrincipal UserPrincipal user) {
        Account account = accountService.getAccountById(user.getAccountId());
        return ResponseEntity.ok(account);
    }

    @Operation(
            summary = "Obtener datos de un usuario mediante su Alias o CVU",
            description = "Se ingresa el CVU o Alias de una cuenta para obtener el nombre completo del usuario y poder confirmar la identidad del destinatario."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "DTO con el nombre completo de un usuario y su alias y CVU",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = AccountPublicDataResponse.class))),
            @ApiResponse(responseCode = "404", description = "No se encontraron cuentas con ese Alias o CVU")
    })
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/search/{identifier}")
    public ResponseEntity<AccountPublicDataResponse> getByDestination(
            @PathVariable String identifier,
            @AuthenticationPrincipal UserPrincipal user
    ) {
        AccountPublicDataResponse account = accountService.getAccountPublicData(identifier, user.getAccountId());
        return ResponseEntity.ok(account);
    }

    @Operation(
            summary = "Eliminar cuenta mediante su Id (Solo Admin)",
            description = "Se realiza una baja logica de la cuenta."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Cuenta dada de baja"),
            @ApiResponse(responseCode = "404", description = "No se encontraron cuentas con ese ID")
    })
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("admin/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable  Long id) {
        accountService.logicallyDeleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Actualizar alias",
            description = "El usuario actualiza el alias de su cuenta, en caso de que quiera reemplazar el alias génerico."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Alias actualizado")
    })
    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/alias")
    public ResponseEntity<Void> updateAlias(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AliasUpdateRequest request
    ) {
        accountService.updateAlias(principal.getId(), request.alias());
        return ResponseEntity.noContent().build();
    }

}
