package services;

import com.billetera_virtual.account.application.AccountService;
import com.billetera_virtual.account.domain.Account;
import com.billetera_virtual.account.domain.port.AccountRepositoryPort;
import com.billetera_virtual.exceptions.domain.EntityInactiveException;
import com.billetera_virtual.exceptions.domain.InvalidAmountException;
import com.billetera_virtual.exceptions.domain.InvalidRequestException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepositoryPort accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void makeTransaction_ShouldSuccess_WhenDataIsValid() {
        // Arrange
        Long originAccountId = 1L;
        Long counterpartyAccountId = 2L;
        BigDecimal amount = new BigDecimal("50.00");

        Account originAccount = new Account(originAccountId, null, null, null, new BigDecimal("100.00"), true);
        Account counterpartyAccount = new Account(counterpartyAccountId, null, null, null, new BigDecimal("20.00"), true);

        when(accountRepository.getById(originAccountId)).thenReturn(Optional.of(originAccount));
        when(accountRepository.getById(counterpartyAccountId)).thenReturn(Optional.of(counterpartyAccount));

        // Act
        BigDecimal resultingBalance = accountService.makeTransaction(originAccountId, counterpartyAccountId, amount);

        // Assert
        assertEquals(new BigDecimal("50.00"), resultingBalance);
        assertEquals(new BigDecimal("50.00"), originAccount.getBalance()); // 100 -50 = 50
        assertEquals(new BigDecimal("70.00"), counterpartyAccount.getBalance()); // 20 + 50 = 70

        verify(accountRepository, times(1)).save(originAccount);
        verify(accountRepository, times(1)).save(counterpartyAccount);

    }


    @Test
    void makeTransaction_ShouldThrowException_WhenAmountIsInvalid() {
        // Arrange
        Long originAccountId = 1L;
        Account disabledAccount = new Account(1L, null, null, null, new BigDecimal("100.00"), false);

        when(accountRepository.getById(originAccountId)).thenReturn(Optional.of(disabledAccount));

        // Act & Assert
        assertThrows(EntityInactiveException.class, () -> {
            accountService.makeTransaction(originAccountId, 2L, new BigDecimal("50.00"));
        });

    }

    @Test
    void makeTransaction_ShouldThrowException_WhenBothAccountsAreTheSame() {
        // Arrange
        Long originAccountId = 1L;

        // Act & Assert
        // Repito el Id para forzar el error y que se arroje la excepcion
        assertThrows(InvalidRequestException.class, () -> {
            accountService.makeTransaction(originAccountId, originAccountId, new BigDecimal("50.00"));
        });

    }

    @Test
    void makeTransaction_ShouldThrowException_WhenAmountIsNegative() {
        // Arrange
        Long originAccountId = 1L;
        BigDecimal amountNegative = new BigDecimal("-50.00");

        // Act & Assert
        assertThrows(InvalidAmountException.class, () -> {
            accountService.makeTransaction(originAccountId, 2L, amountNegative);
        });
    }

    @Test
    void makeTransaction_ShouldThrowException_WhenAmountIsZero() {
        // Arrange
        BigDecimal zeroAmount = BigDecimal.ZERO;

        // Act & Assert
        assertThrows(InvalidAmountException.class, () -> {
            accountService.makeTransaction(1L, 2L, zeroAmount);
        });
    }

}
