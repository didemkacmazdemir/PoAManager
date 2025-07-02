package nl.rabobank.service;

import nl.rabobank.account.Account;
import nl.rabobank.account.PaymentAccount;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.mapper.PowerOfAttorneyMapper;
import nl.rabobank.model.request.PowerOfAttorneyRequest;
import nl.rabobank.model.response.PowerOfAttorneyResponse;
import nl.rabobank.mongo.model.exception.AccountNotFoundException;
import nl.rabobank.mongo.model.exception.DuplicateAuthorizationException;
import nl.rabobank.mongo.service.AccountDataService;
import nl.rabobank.mongo.service.PowerOfAttorneyDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PowerAttorneyApiServiceTest {

    @Mock
    private AccountDataService accountDataService;

    @Mock
    private PowerOfAttorneyDataService powerOfAttorneyDataService;

    @Mock
    private PowerOfAttorneyMapper powerOfAttorneyMapper;

    @InjectMocks
    private PowerAttorneyApiService powerAttorneyApiService;

    @Test
    void createPowerOfAttorney_shouldCreateAndReturnResponse() {
        String accountNumber = "123456";
        String grantorName = "grantor";
        String granteeName = "grantee";
        Authorization authorization = Authorization.READ;

        PowerOfAttorneyRequest request = new PowerOfAttorneyRequest(grantorName, granteeName, authorization, accountNumber);
        Account mockAccount = new PaymentAccount(accountNumber, "holder", 100.0);
        PowerOfAttorney mockPowerOfAttorney = PowerOfAttorney.builder()
                .grantorName(grantorName)
                .granteeName(granteeName)
                .authorization(authorization)
                .account(mockAccount)
                .build();

        when(accountDataService.getAccountByAccountNumber(accountNumber)).thenReturn(mockAccount);

        PowerOfAttorneyResponse response = powerAttorneyApiService.createPowerOfAttorney(request);

        assertNotNull(response);
        assertEquals(grantorName, response.grantorName());
        assertEquals(accountNumber, response.accountNumber());
        assertEquals(authorization, response.authorization());

        verify(powerOfAttorneyDataService).checkPOAExistsByGrantorNameAndGranteeNameAndAccountNumber(
                grantorName, granteeName, accountNumber);
        verify(accountDataService).getAccountByAccountNumber(accountNumber);
        verify(powerOfAttorneyDataService).savePowerOfAttorney(mockPowerOfAttorney);
    }

    @Test
    void createPowerOfAttorney_shouldThrowWhenDuplicateExists() {
        String accountNumber = "123456";
        String grantorName = "grantor";
        String granteeName = "grantee";
        Authorization authorization = Authorization.READ;

        PowerOfAttorneyRequest request = new PowerOfAttorneyRequest(grantorName, granteeName, authorization, accountNumber);

        doThrow(new DuplicateAuthorizationException("Exists"))
                .when(powerOfAttorneyDataService)
                .checkPOAExistsByGrantorNameAndGranteeNameAndAccountNumber(grantorName, granteeName, accountNumber);

        assertThrows(DuplicateAuthorizationException.class,
                () -> powerAttorneyApiService.createPowerOfAttorney(request));

        verify(accountDataService, never()).getAccountByAccountNumber(anyString());
        verify(powerOfAttorneyDataService, never()).savePowerOfAttorney(any());
    }

    @Test
    void createPowerOfAttorney_shouldThrowWhenAccountNotFound() {
        String accountNumber = "123456";
        String grantorName = "grantor";
        String granteeName = "grantee";
        Authorization authorization = Authorization.READ;

        PowerOfAttorneyRequest request = new PowerOfAttorneyRequest(grantorName, granteeName, authorization, accountNumber);

        when(accountDataService.getAccountByAccountNumber(accountNumber))
                .thenThrow(new AccountNotFoundException("Not found"));

        assertThrows(AccountNotFoundException.class,
                () -> powerAttorneyApiService.createPowerOfAttorney(request));

        verify(powerOfAttorneyDataService).checkPOAExistsByGrantorNameAndGranteeNameAndAccountNumber(
                grantorName, granteeName, accountNumber);
        verify(powerOfAttorneyDataService, never()).savePowerOfAttorney(any());
    }

}