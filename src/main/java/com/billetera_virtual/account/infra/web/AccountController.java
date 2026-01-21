package com.billetera_virtual.account.infra.web;

import com.billetera_virtual.account.domain.Account;
import com.billetera_virtual.account.domain.port.AccountServicePort;
import com.billetera_virtual.account.infra.web.dto.AccountRequest;
import com.billetera_virtual.account.infra.web.dto.AccountUpdateRequest;
import com.billetera_virtual.account.infra.web.dto.AliasUpdateRequest;
import com.billetera_virtual.account.infra.web.mapper.AccountMapperWeb;
import com.billetera_virtual.security.infra.model.UserPrincipal;
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
    @GetMapping("admin/{id}")
    public ResponseEntity<Account> getById(@PathVariable Long id) {
        Account account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("admin/all")
    public ResponseEntity<List<Account>> getAll() {
        List<Account> account = accountService.getAll();
        return ResponseEntity.ok(account);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/me")
    public ResponseEntity<Account> getByAccountId(@AuthenticationPrincipal UserPrincipal user) {
        Account account = accountService.getAccountById(user.getAccountId());
        return ResponseEntity.ok(account);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/alias/{alias}")
    public ResponseEntity<Account> getByAlias(@PathVariable String alias) {
        Account account = accountService.getAccountByAlias(alias);
        // Account response = userMapperWeb.toResponse(user); mapear a cuenta
        return ResponseEntity.ok(account);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("admin/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable  Long id) {
        accountService.logicallyDeleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("admin/{id}")
    public ResponseEntity<Account> updateAccount(
            @RequestBody AccountUpdateRequest request,
            @PathVariable Long id) {
        Account account = accountService.updateAccount(id, request.amount());
        return ResponseEntity.ok(account);
    }


    @PreAuthorize("hasRole('USER')")
    @PatchMapping("/update/alias")
    public ResponseEntity<Void> updateAlias(
            @AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody AliasUpdateRequest request
    ) {
        accountService.updateAlias(principal.getId(), request.alias());
        return ResponseEntity.noContent().build();
    }

}
