package com.cuenta_bancaria.cuenta.infra.web;

import com.cuenta_bancaria.cuenta.domain.Account;
import com.cuenta_bancaria.cuenta.domain.port.AccountServicePort;
import com.cuenta_bancaria.cuenta.infra.web.dto.AccountRequest;
import com.cuenta_bancaria.cuenta.infra.web.dto.AccountUpdateRequest;
import com.cuenta_bancaria.cuenta.infra.web.mapper.AccountMapperWeb;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cuenta")
public class AccountController {

    private final AccountServicePort accountService;
    private final AccountMapperWeb accountMapperWeb;

    @PostMapping("/crear")
    public ResponseEntity<Account> crearCuenta(@RequestBody AccountRequest c) {
        Account cuentaDomain = accountMapperWeb.toDomain(c);
        Account cuenta = accountService.createAccount(cuentaDomain);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cuenta.getId())
                .toUri();
        return ResponseEntity.created(location).body(cuenta);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getById(@PathVariable Long id) {
        Account cuenta = accountService.getAccountById(id);
        return ResponseEntity.ok(cuenta);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAll() {
        List<Account> cuenta = accountService.getAll();
        return ResponseEntity.ok(cuenta);
    }

    @GetMapping("/usuario/{idUser}")
    public ResponseEntity<Account> getByIdUser(@PathVariable Long idUser) {
        Account cuenta = accountService.getAccountByIdUser(idUser);
        return ResponseEntity.ok(cuenta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuenta(@PathVariable  Long id) {
        accountService.logicallyDeleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateCuenta(
            @RequestBody AccountUpdateRequest c,
            @PathVariable Long id) {
        Account cuenta = accountService.updateAccount(id, c.getAmount());
        return ResponseEntity.ok(cuenta);
    }

}
