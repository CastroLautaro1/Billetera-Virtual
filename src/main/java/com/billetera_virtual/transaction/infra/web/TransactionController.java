package com.billetera_virtual.transaction.infra.web;

import com.billetera_virtual.security.infra.model.UserPrincipal;
import com.billetera_virtual.transaction.domain.Transaction;
import com.billetera_virtual.transaction.domain.port.TransactionServicePort;
import com.billetera_virtual.transaction.infra.web.dto.TransactionDTO;
import com.billetera_virtual.transaction.infra.web.mapper.TransactionMapperWeb;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
            @Valid @RequestBody TransactionDTO dto,
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
    public ResponseEntity<Page<Transaction>> getAllByAccountId(
            @AuthenticationPrincipal UserPrincipal principal,
            @PageableDefault(size = 10, sort = "timestamp", direction = Sort.Direction.DESC) Pageable pageable
    ) {
        Long accountId = principal.getAccountId();
        Page<Transaction> transactions = transactionService.getAllByAccountId(accountId, pageable);
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
