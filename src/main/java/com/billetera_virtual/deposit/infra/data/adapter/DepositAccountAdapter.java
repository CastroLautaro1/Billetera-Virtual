package com.billetera_virtual.deposit.infra.data.adapter;

import com.billetera_virtual.account.domain.Account;
import com.billetera_virtual.account.domain.port.AccountRepositoryPort;
import com.billetera_virtual.deposit.domain.dto.AccountUpdateResultDTO;
import com.billetera_virtual.deposit.domain.ports.DepositAccountPort;
import com.billetera_virtual.exceptions.domain.EntityNotFoundException;
import com.billetera_virtual.user.domain.User;
import com.billetera_virtual.user.domain.port.UserRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class DepositAccountAdapter implements DepositAccountPort {

    private final AccountRepositoryPort accountRepository;
    private final UserRepositoryPort userRepository;

    @Override
    public AccountUpdateResultDTO addBalanceByEmail(String email, BigDecimal amount) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("El email no coincide con ningun usuario"));

        Account account = accountRepository.getByIdUser(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("El ID del usuario no coincide con ninguna cuenta"));

        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        return new AccountUpdateResultDTO(account.getId(), account.getBalance());
    }
}
