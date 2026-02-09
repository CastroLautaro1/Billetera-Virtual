package com.billetera_virtual.account.domain.port;

import com.billetera_virtual.account.domain.Account;
import com.billetera_virtual.account.domain.dto.AccountPublicDataResponse;

import java.math.BigDecimal;
import java.util.List;

public interface AccountServicePort {
    Account createAccount(Account account);
    Account getAccountById(Long id);
    Account getAccountByIdUser(Long idUser);
    Account getAccountByAlias(String alias);
    Account getAccountByCvu(String cvu);
    AccountPublicDataResponse getAccountPublicData(String identifier, Long accountId);
    List<Account> getAll();
    Account updateAccount(Long id, BigDecimal balance);
    void updateAlias(Long id, String alias);
    void logicallyDeleteById(Long id);
    Account createAccountFromUser(Long userId);
    BigDecimal makeTransaction(Long originId, Long counterpartyId, BigDecimal amount);
    Long getAccountIdByUserId(Long userId);
    Long getAccountIdByAlias(String alias);
    Long getAccountIdByDestination(String destination);
}
