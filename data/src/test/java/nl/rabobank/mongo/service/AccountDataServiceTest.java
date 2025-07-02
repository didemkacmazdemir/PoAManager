package nl.rabobank.mongo.service;

import nl.rabobank.account.Account;
import nl.rabobank.mongo.document.AccountDocument;
import nl.rabobank.mongo.model.AccountType;
import nl.rabobank.mongo.model.exception.AccountNotFoundException;
import nl.rabobank.mongo.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountDataServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountDataService accountDataService;

    @Test
    void getAccountByAccountNumber_shouldReturnAccountWhenExists() {
        String accountNumber = "123456";
        AccountDocument document = new AccountDocument();
        document.setId("id");
        document.setAccountNumber(accountNumber);
        document.setAccountHolderName("John Doe");
        document.setBalance(1000.0);
        document.setAccountType(AccountType.PAYMENT);

        when(accountRepository.findByAccountNumber(accountNumber))
                .thenReturn(Optional.of(document));

        Account result = accountDataService.getAccountByAccountNumber(accountNumber);

        assertNotNull(result);
        assertEquals(accountNumber, result.getAccountNumber());
        assertEquals("John Doe", result.getAccountHolderName());
        assertEquals(1000.0, result.getBalance());

        verify(accountRepository).findByAccountNumber(accountNumber);
    }

    @Test
    void getAccountByAccountNumber_shouldThrowExceptionWhenNotFound() {
        String accountNumber = "999999";
        when(accountRepository.findByAccountNumber(accountNumber))
                .thenReturn(Optional.empty());

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class,
                () -> accountDataService.getAccountByAccountNumber(accountNumber));

        assertEquals("Account with number 999999 not found", exception.getMessage());
        verify(accountRepository).findByAccountNumber(accountNumber);
    }

    @Test
    void getAccountByAccountNumber_shouldCorrectlyMapDocumentToDomain() {
        String accountNumber = "654321";
        AccountDocument document = new AccountDocument();
        document.setId("id");
        document.setAccountNumber(accountNumber);
        document.setAccountHolderName("John Doe");
        document.setBalance(1000.0);
        document.setAccountType(AccountType.PAYMENT);

        when(accountRepository.findByAccountNumber(accountNumber))
                .thenReturn(Optional.of(document));

        Account result = accountDataService.getAccountByAccountNumber(accountNumber);

        assertEquals(document.getAccountNumber(), result.getAccountNumber());
        assertEquals(document.getAccountHolderName(), result.getAccountHolderName());
        assertEquals(document.getBalance(), result.getBalance(), 0.001);
    }

}