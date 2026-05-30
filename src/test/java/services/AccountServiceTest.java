package services;

import com.billetera_virtual.account.application.AccountService;
import com.billetera_virtual.account.domain.Account;
import com.billetera_virtual.account.domain.dto.AccountPublicDataResponse;
import com.billetera_virtual.account.domain.dto.UserDataDTO;
import com.billetera_virtual.account.domain.port.AccountRepositoryPort;
import com.billetera_virtual.account.domain.port.external.UserDataPort;
import com.billetera_virtual.exceptions.domain.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest {

    @Mock
    private AccountRepositoryPort accountRepository;

    @Mock
    private UserDataPort userDataPort;

    @InjectMocks
    private AccountService accountService;

    // --- TESTS PARA EL METODO makeTransaction ---
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
    void makeTransaction_ShouldThrowException_WhenOriginAccountIsDisable() {
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
    void makeTransaction_ShouldThrowException_WhenDestinationAccountIsDisable() {
        // Arrange
        Long originAccountId = 1L;
        Long destinationAccountId = 2L;
        Account originAccount = new Account(1L, null, null, null, new BigDecimal("100.00"), true);
        Account destinationAccount = new Account(destinationAccountId, 2L, null, null, new BigDecimal("100.00"), false);

        // Act
        when(accountRepository.getById(originAccountId)).thenReturn(Optional.of(originAccount));
        when(accountRepository.getById(destinationAccountId)).thenReturn(Optional.of(destinationAccount));

        // Assert
        assertThrows(EntityInactiveException.class, () -> {
            accountService.makeTransaction(originAccountId, destinationAccountId, new BigDecimal("20.00"));
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

    @Test
    void makeTransaction_ShouldThrowException_WhenInsufficientBalance() {
        // Arrange
        Long accountId = 1L;
        Account poorAccount = new Account(1L, null, null, null, new BigDecimal("100.00"), true);

        // Act
        when(accountRepository.getById(accountId)).thenReturn(Optional.of(poorAccount));

        // Assert
        assertThrows(InsufficientBalanceException.class, () -> {
            accountService.makeTransaction(accountId, 2L, new BigDecimal("200.00"));
        });
    }

    // ---- TESTS PARA EL METODO getAccountById ----
    @Test
    void getAccountById_ShouldReturnAccount_WhenIdIsValid() {
        // Act
        Long accountId = 1L;
        Account account = new Account(accountId, 1L, null, null, new BigDecimal("100.00"), true);

        when(accountRepository.getById(accountId)).thenReturn(Optional.of(account));

        // Act
        Account accountFound = accountService.getAccountById(accountId);

        // Assert
        assertEquals(accountFound.getId(), accountId);
        assertEquals(accountFound.getUser_id(), account.getUser_id());
        assertEquals(accountFound.getBalance(), account.getBalance());

        verify(accountRepository, times(1)).getById(accountId);
    }

    @Test
    void getAccountByIdUser_ShouldThrowException_WhenIdDoesNotExist() {
        // Arrange
        Long accountId = 99L;

        when(accountRepository.getById(accountId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            accountService.getAccountById(accountId);
        });

        assertEquals("El ID ingresado no coincide con ninguna cuenta", exception.getMessage());

        verify(accountRepository, times(1)).getById(accountId);
    }

    // ---- TESTS PARA EL METODO getAccountByIdUser ----

    @Test
    void getAccountByIdUser_ShouldReturnAccount_WhenUserIdIsValid() {
        //Arrange
        Long userId = 1L;

        Account account = new Account(1L, userId, null, null, new BigDecimal("50.00"), true);

        //Act
        when(accountRepository.getByIdUser(userId)).thenReturn(Optional.of(account));
        Account accountFound = accountService.getAccountByIdUser(userId);

        //Assert
        assertEquals(accountFound.getUser_id(), userId);
        assertEquals(accountFound.getId(), account.getId());
        assertEquals(accountFound.getBalance(), account.getBalance());

        verify(accountRepository, times(1)).getByIdUser(userId);
    }

    @Test
    void getAccountByIdUser_ShouldThrowException_WhenIdUserDoesNotExist() {
        // Arrange
        Long userId = 99L;

        when(accountRepository.getByIdUser(userId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            accountService.getAccountByIdUser(userId);
        });

        assertEquals("El ID ingresado no coincide con ninguna cuenta", exception.getMessage());

        verify(accountRepository, times(1)).getByIdUser(userId);
    }

    // ---- TESTS PARA EL METODO getAccountPublicData ----
    @Test
    void getAccountPublicData_ShouldSuccess_WhenDataIsValid() {
        // Arrange
        String identifier = "alias";
        Long authenticatedAccountId = 1L;
        Long destinationId = 2L;
        Long userId = 10L;

        Account account = new Account(destinationId, userId, null, "alias", null, true);
        UserDataDTO mockUserData = new UserDataDTO("User", "Test");

        when(accountRepository.getAccountByAlias(identifier)).thenReturn(Optional.of(account));
        when(accountRepository.getById(destinationId)).thenReturn(Optional.of(account));
        when(userDataPort.getUserDataById(userId)).thenReturn(mockUserData);

        // Act
        AccountPublicDataResponse accountResponse = accountService.getAccountPublicData(identifier, authenticatedAccountId);

        // Assert
        assertNotNull(accountResponse, "La respuesta no deberia ser nula");
        assertEquals(destinationId, accountResponse.accountId());
        assertEquals("User", accountResponse.firstname());
        assertEquals("Test", accountResponse.lastname());
        assertEquals("alias", accountResponse.alias());
        assertEquals(null, accountResponse.cvu());

        verify(accountRepository, times(1)).getAccountByAlias(identifier);
        verify(accountRepository, times(1)).getById(destinationId);
        verify(userDataPort, times(1)).getUserDataById(userId);

    }

    @Test
    void getAccountPublicData_ShouldThrowException_WhenAliasHasNoCoincidence() {
        // Arrange
        String identifier = "unexistent";
        Long authenticatedAccountId = 1L;

        when(accountRepository.getAccountByAlias(identifier)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            accountService.getAccountPublicData(identifier, authenticatedAccountId);
        });

        assertEquals("El alias no coincide con ninguna Cuenta", exception.getMessage());

        verify(accountRepository, times(1)).getAccountByAlias(identifier);
    }

    @Test
    void getAccountPublicData_ShouldThrowException_WhenCVUHasNoCoincidence() {
        // Arrange
        String identifier = "0000000000000000000000";
        Long authenticatedAccountId = 1L;

        when(accountRepository.getAccountByCvu(identifier)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            accountService.getAccountPublicData(identifier, authenticatedAccountId);
        });

        assertEquals("CVU no encontrado", exception.getMessage());

        verify(accountRepository, times(1)).getAccountByCvu(identifier);
    }

    @Test
    void getAccountPublicData_ShouldThrowException_WhenAccountIsDisable() {
        // Arrange
        Account account = new Account(2L, 2L, null, "alias", null, false);
        String identifier = "alias";
        Long destinationId = 2L;
        Long authenticatedAccountId = 1L;

        when(accountRepository.getAccountByAlias(identifier)).thenReturn(Optional.of(account));
        when(accountRepository.getById(destinationId)).thenReturn(Optional.of(account));

        // Act & Assert
        EntityInactiveException exception = assertThrows(EntityInactiveException.class, () -> {
            accountService.getAccountPublicData(identifier, authenticatedAccountId);
        });

        assertEquals("La cuenta buscada se encuentra deshabilitada.", exception.getMessage());

        verify(accountRepository, times(1)).getById(destinationId);

    }

    @Test
    void getAccountPublicData_ShouldThrowException_WhenAccountIsAutoSearch() {
        // Arrange
        Account account = new Account(1L, 1L, null, "alias", null, true);
        String identifier = "alias";
        Long destinationId = 1L;
        Long authenticatedAccountId = 1L;

        when(accountRepository.getAccountByAlias(identifier)).thenReturn(Optional.of(account));
        when(accountRepository.getById(destinationId)).thenReturn(Optional.of(account));

        // Act & Assert
        InvalidRequestException exception = assertThrows(InvalidRequestException.class, () -> {
            accountService.getAccountPublicData(identifier, authenticatedAccountId);
        });

        assertEquals("No puedes realizar una transferencia a tu propia cuenta", exception.getMessage());
    }

    // ---- TESTS PARA EL METODO getAccountPublicDataById ----
    @Test
    void getAccountPublicDataById_ShouldSuccess_WhenDataIsValid() {
        // Arrange
        Account account = new Account(1L, 1L, null, "alias", null, true);
        UserDataDTO mockUserData = new UserDataDTO("Juan", "Perez");
        Long accountId = 1L;

        when(accountRepository.getById(accountId)).thenReturn(Optional.of(account));
        when(userDataPort.getUserDataById(account.getUser_id())).thenReturn(mockUserData);

        // Act
        AccountPublicDataResponse accountData = accountService.getAccountPublicDataById(accountId);

        // Assert
        assertEquals(account.getId(), accountData.accountId());
        assertEquals(mockUserData.firstname(), accountData.firstname());
        assertEquals(mockUserData.lastname(), accountData.lastname());
        assertEquals(account.getAlias(), accountData.alias());

        verify(accountRepository, times(1)).getById(accountId);
        verify(userDataPort, times(1)).getUserDataById(account.getUser_id());
    }

    @Test
    void getAccountPublicDataById_ShouldThrowException_WhenAccountIsDisable() {
        Account account = new Account(1L, 1L, null, "alias", null, false);
        Long accountId = 1L;

        when(accountRepository.getById(accountId)).thenReturn(Optional.of(account));

        // Act & Assert
        EntityInactiveException exception = assertThrows(EntityInactiveException.class, () -> {
            accountService.getAccountPublicDataById(accountId);
        });

        assertEquals("La cuenta se encuentra dada de baja.", exception.getMessage());

        verify(accountRepository, times(1)).getById(accountId);
    }

    @Test
    void getAccountPublicDataById_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        Account account = new Account(1L, 1L, null, "alias", null, true);
        Long accountId = 1L;
        Long userId = 1L;

        when(accountRepository.getById(accountId)).thenReturn(Optional.of(account));
        when(userDataPort.getUserDataById(userId)).thenThrow(
                new EntityNotFoundException("No se encontró el usuario con ID: " + userId)
        );

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            accountService.getAccountPublicDataById(accountId);
        });

        assertEquals("No se encontró el usuario con ID: " + userId, exception.getMessage());

        verify(accountRepository, times(1)).getById(accountId);
        verify(userDataPort, times(1)).getUserDataById(userId);
    }

    // --- TESTS PARA EL METODO getAccountReceipt ---
    @Test
    void getAccountReceipt_ShoudlSuccess_WhenDataIsValid() {
        // Arrange
        Account account = new Account(1L, 1L, null, "alias", null, true);
        UserDataDTO mockUserData = new UserDataDTO("User", "Test");
        Long accountId = 1L;

        when(accountRepository.getById(accountId)).thenReturn(Optional.of(account));
        when(userDataPort.getUserDataById(account.getUser_id())).thenReturn(mockUserData);

        // Act
        AccountPublicDataResponse accountData = accountService.getAccountReceipt(accountId);

        // Assert
        assertEquals(accountData.accountId(), accountId);
        assertEquals(accountData.firstname(), mockUserData.firstname());
        assertEquals(accountData.lastname(), mockUserData.lastname());
        assertEquals(accountData.alias(), account.getAlias());
        assertEquals(accountData.cvu(), account.getCvu());

        verify(accountRepository, times(1)).getById(accountId);
        verify(userDataPort, times(1)).getUserDataById(account.getUser_id());
    }

    @Test
    void getAccountReceipt_ShouldThrowException_WhenAccountNotFound() {
        // Arrange
        Long accountId = 1L;

        when(accountRepository.getById(accountId)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            accountService.getAccountReceipt(accountId);
        });

        assertEquals("El ID ingresado no coincide con ninguna cuenta", exception.getMessage());

        verify(accountRepository, times(1)).getById(accountId);
    }

    @Test
    void getAccountReceipt_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        Account account = new Account(1L, 1L, null, "alias", null, true);
        Long accountId = 1L;
        Long userId = 1L;

        when(accountRepository.getById(accountId)).thenReturn(Optional.of(account));
        when(userDataPort.getUserDataById(userId)).thenThrow(
                new EntityNotFoundException("No se encontró el usuario con ID: " + userId)
        );

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            accountService.getAccountReceipt(accountId);
        });

        assertEquals("No se encontró el usuario con ID: " + userId, exception.getMessage());

        verify(accountRepository, times(1)).getById(accountId);
        verify(userDataPort, times(1)).getUserDataById(userId);
    }

    // --- TESTS PARA EL METODO getAccountByAlias
    @Test
    void getAccountByAlias_ShouldSuccess_WhenDataIsValid() {
        // Arrange
        Account mockAccount = new Account(1L, 1L, null, "alias", null, true);
        String alias = "alias";

        when(accountRepository.getAccountByAlias(alias)).thenReturn(Optional.of(mockAccount));

        // Act
        Account account = accountService.getAccountByAlias(alias);

        // Assert
        assertEquals(mockAccount.getId(), account.getId());
        assertEquals(mockAccount.getUser_id(), account.getUser_id());
        assertEquals(mockAccount.getAlias(), account.getAlias());
        assertEquals(mockAccount.isStatus(), account.isStatus());

        verify(accountRepository, times(1)).getAccountByAlias(alias);
    }

    @Test
    void getAccountByAlias_ShouldThrowException_WhenAccountNotFound() {
        // Arrange
        String alias = "unexistent";

        when(accountRepository.getAccountByAlias(alias)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            accountService.getAccountByAlias(alias);
        });

        assertEquals("El alias no coincide con ninguna Cuenta", exception.getMessage());

        verify(accountRepository, times(1)).getAccountByAlias(alias);
    }

    // --- TESTS PARA EL METODO getAccountByCVU
    @Test
    void getAccountByCVU_ShouldSuccess_WhenDataIsValid() {
        // Arrange
        Account mockAccount = new Account(1L, 1L, "0000000000000000123456", null, null, true);
        String cvu = "0000000000000000123456";

        when(accountRepository.getAccountByCvu(cvu)).thenReturn(Optional.of(mockAccount));

        // Act
        Account account = accountService.getAccountByCvu(cvu);

        // Assert
        assertEquals(mockAccount.getId(), account.getId());
        assertEquals(mockAccount.getUser_id(), account.getUser_id());
        assertEquals(mockAccount.getCvu(), account.getCvu());
        assertEquals(mockAccount.isStatus(), account.isStatus());

        verify(accountRepository, times(1)).getAccountByCvu(cvu);
    }

    @Test
    void getAccountByCVU_ShouldThrowException_WhenAccountNotFound() {
        // Arrange
        String cvu = "0000000000000000123456";

        when(accountRepository.getAccountByCvu(cvu)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            accountService.getAccountByCvu(cvu);
        });

        assertEquals("El CVU no coincide con ninguna Cuenta", exception.getMessage());

        verify(accountRepository, times(1)).getAccountByCvu(cvu);
    }

    // --- TESTS PARA EL METODO getAccountIdByAlias
    @Test
    void getAccountIdByAlias_ShouldSuccess_WhenDataIsValid() {
        // Arrange
        Account mockAccount = new Account(1L, 1L, null, "alias", null, true);
        Long accountId = 1L;
        String alias = "alias";

        when(accountRepository.getAccountByAlias(alias)).thenReturn(Optional.of(mockAccount));

        // Act
        Long accountIdResponse = accountService.getAccountIdByAlias(alias);

        // Assert
        assertEquals(accountIdResponse, accountId);

        verify(accountRepository, times(1)).getAccountByAlias(alias);
    }

    @Test
    void getAccountIdByAlias_ShouldThrowException_WhenAccountNotFound() {
        // Arrange
        String alias = "unexistent";

        when(accountRepository.getAccountByAlias(alias)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            accountService.getAccountIdByAlias(alias);
        });

        assertEquals("El alias no coincide con ninguna Cuenta", exception.getMessage());

        verify(accountRepository, times(1)).getAccountByAlias(alias);
    }

    // --- TESTS PARA EL METODO getAccountIdByDestination
    @Test
    void getAccountIdByDestination_ShouldSuccess_WhenAliasIsValid() {
        // Arrange
        Long accountId = 1L;
        Account mockAccount = new Account(1L, 1L, null, "alias", null, true);
        String alias = "alias";

        when(accountRepository.getAccountByAlias(alias)).thenReturn(Optional.of(mockAccount));

        // Act
        Long accountIdResponse = accountService.getAccountIdByDestination(alias);

        // Assert
        assertEquals(accountId, accountIdResponse);

        verify(accountRepository, times(1)).getAccountByAlias(alias);
    }

    @Test
    void getAccountIdByDestination_ShouldSuccess_WhenCvuIsValid() {
        // Arrange
        Long accountId = 1L;
        Account mockAccount = new Account(1L, 1L, "0000000000000000123456", "alias", null, true);
        String cvu = "0000000000000000123456";

        when(accountRepository.getAccountByCvu(cvu)).thenReturn(Optional.of(mockAccount));

        // Act
        Long accountIdResponse = accountService.getAccountIdByDestination(cvu);

        // Assert
        assertEquals(accountId, accountIdResponse);

        verify(accountRepository, times(1)).getAccountByCvu(cvu);
    }

    @Test
    void getAccountIdByDestination_ShouldThrowException_WhenAccountNotFound() {
        // Arrange
        String alias = "unexistent";

        when(accountRepository.getAccountByAlias(alias)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            accountService.getAccountIdByDestination(alias);
        });

        assertEquals(exception.getMessage(), "El alias no coincide con ninguna Cuenta");

        verify(accountRepository, times(1)).getAccountByAlias(alias);
    }

    @Test
    void getAccountIdByDestination_ShouldThrowException_WhenCvuNotFound() {
        // Arrange
        String cvu = "0000000000000000123456";

        when(accountRepository.getAccountByCvu(cvu)).thenReturn(Optional.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            accountService.getAccountIdByDestination(cvu);
        });

        assertEquals(exception.getMessage(), "CVU no encontrado");

        verify(accountRepository, times(1)).getAccountByCvu(cvu);
    }

    // --- TESTS PARA EL METODO getAll
    // No se como probarlo

    // --- TESTS PARA EL METODO logicallyDeleteById
    @Test
    void logicallyDeleteById_ShouldSuccess_WhenDataIsValid() {
        // Arrange
        Long accountId = 1L;

        when(accountRepository.existsById(accountId)).thenReturn(true);

        // Act
        accountService.logicallyDeleteById(accountId);

        // Assert
        verify(accountRepository, times(1)).existsById(accountId);

        verify(accountRepository, times(1)).logicallyDeleteById(accountId);
    }

    @Test
    void logicallyDeleteById_ShouldThrowException_WhenAccountNotFound() {
        // Arrange
        Long accountId = 1L;

        when(accountRepository.existsById(accountId)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            accountService.logicallyDeleteById(accountId);
        });

        assertEquals(exception.getMessage(), "El ID ingresado no coincide con ninguna cuenta");

        verify(accountRepository, times(1)).existsById(accountId);
        verify(accountRepository, never()).logicallyDeleteById(anyLong());
    }
}
