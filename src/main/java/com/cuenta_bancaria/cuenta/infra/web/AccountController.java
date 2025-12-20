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
@RequestMapping("/account")
public class AccountController {

    private final AccountServicePort accountService;
    private final AccountMapperWeb accountMapperWeb;

    @PostMapping("/create")
    public ResponseEntity<Account> crearCuenta(@RequestBody AccountRequest request) {
        Account accountDomain = accountMapperWeb.toDomain(request);
        Account account = accountService.createAccount(accountDomain);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(account.getId())
                .toUri();
        return ResponseEntity.created(location).body(account);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getById(@PathVariable Long id) {
        Account account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAll() {
        List<Account> account = accountService.getAll();
        return ResponseEntity.ok(account);
    }

    @GetMapping("/user/{idUser}")
    public ResponseEntity<Account> getByIdUser(@PathVariable Long idUser) {
        Account account = accountService.getAccountByIdUser(idUser);
        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCuenta(@PathVariable  Long id) {
        accountService.logicallyDeleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> updateCuenta(
            @RequestBody AccountUpdateRequest request,
            @PathVariable Long id) {
        Account account = accountService.updateAccount(id, request.getAmount());
        return ResponseEntity.ok(account);
    }

}
