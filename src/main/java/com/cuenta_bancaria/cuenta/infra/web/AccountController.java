package com.cuenta_bancaria.cuenta.infra.web;

import com.cuenta_bancaria.cuenta.domain.Account;
import com.cuenta_bancaria.cuenta.domain.port.AccountServicePort;
import com.cuenta_bancaria.cuenta.infra.web.dto.AccountRequest;
import com.cuenta_bancaria.cuenta.infra.web.dto.AccountUpdateRequest;
import com.cuenta_bancaria.cuenta.infra.web.mapper.AccountMapperWeb;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Account> getById(@PathVariable Long id) {
        Account account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<Account>> getAll() {
        List<Account> account = accountService.getAll();
        return ResponseEntity.ok(account);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/user/{idUser}")
    public ResponseEntity<Account> getByIdUser(@PathVariable Long idUser) {
        Account account = accountService.getAccountByIdUser(idUser);
        return ResponseEntity.ok(account);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable  Long id) {
        accountService.logicallyDeleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Account> updateAccount(
            @RequestBody AccountUpdateRequest request,
            @PathVariable Long id) {
        Account account = accountService.updateAccount(id, request.amount());
        return ResponseEntity.ok(account);
    }

}
