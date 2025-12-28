package com.cuenta_bancaria.transaction.infra.web;

import com.cuenta_bancaria.security.infra.model.UserPrincipal;
import com.cuenta_bancaria.transaction.domain.Transaction;
import com.cuenta_bancaria.transaction.domain.port.TransactionServicePort;
import com.cuenta_bancaria.transaction.infra.web.dto.TransactionDTO;
import com.cuenta_bancaria.transaction.infra.web.mapper.TransactionMapperWeb;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionServicePort transactionService;
    private final TransactionMapperWeb transactionMapper;

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping("/transfer")
    public ResponseEntity<Transaction> makeTransfer(
            @RequestBody TransactionDTO dto,
            @AuthenticationPrincipal UserPrincipal principal)
    {
        Long userId = principal.getId();
        Transaction transaction = transactionMapper.toDomain(dto);
        Transaction saved = transactionService.makeTransaction(transaction, dto.alias(), userId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(location).body(saved);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getById(@PathVariable Long id) {
        Transaction transaction = transactionService.getById(id);
        return ResponseEntity.ok(transaction);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/filter/all")
    public ResponseEntity<List<Transaction>> getAllByAccountId(@AuthenticationPrincipal UserPrincipal principal) {
        Long accountId = principal.getAccountId();
        List<Transaction> transactions = transactionService.getAllByAccountId(accountId);
        return ResponseEntity.ok(transactions);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/filter/{type}")
    public ResponseEntity<List<Transaction>> filterByType(
            @PathVariable Transaction.TransactionType type,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long accountId = principal.getAccountId();
        List<Transaction> transactions = transactionService.filterByType(type, accountId);
        return ResponseEntity.ok(transactions);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("filter/amount")
    public ResponseEntity<List<Transaction>> filterByAmount(
            @RequestParam(name = "max") double amount,
            @AuthenticationPrincipal UserPrincipal principal) {
        Long accountId = principal.getAccountId();
        List<Transaction> transactions = transactionService.findAllByAmountLessThan(amount, accountId);
        return ResponseEntity.ok(transactions);
    }

}
