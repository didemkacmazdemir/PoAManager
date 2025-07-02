package nl.rabobank.service;

import nl.rabobank.account.Account;
import nl.rabobank.account.PaymentAccount;
import nl.rabobank.account.SavingsAccount;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.model.response.AccesibleAccountResponse;
import nl.rabobank.mongo.service.PowerOfAttorneyDataService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountApiServiceTest {
    @Mock
    private PowerOfAttorneyDataService powerOfAttorneyDataService;

    @InjectMocks
    private AccountApiService accountApiService;

    private static final String GRANTEE_NAME = "testGrantee";
    private static final String ACCOUNT_NUMBER_READ = "ACC123READ";
    private static final String ACCOUNT_NUMBER_WRITE = "ACC456WRITE";
    private static final String ACCOUNT_NUMBER_READ_2 = "ACC789READ";

    @Test
    void getAccessibleAccounts_shouldReturnAccountsWithReadAndWriteAuthorizations() {
        Account readAccount1 = new PaymentAccount(ACCOUNT_NUMBER_READ, "Holder1", 100.0);
        Account writeAccount1 = new SavingsAccount(ACCOUNT_NUMBER_WRITE, "Holder2", 200.0);
        Account readAccount2 = new SavingsAccount(ACCOUNT_NUMBER_READ_2, "Holder3", 300.0);

        PowerOfAttorney poaRead1 = PowerOfAttorney.builder()
                .granteeName(GRANTEE_NAME)
                .grantorName("Grantor1")
                .account(readAccount1)
                .authorization(Authorization.READ)
                .build();

        PowerOfAttorney poaWrite1 = PowerOfAttorney.builder()
                .granteeName(GRANTEE_NAME)
                .grantorName("Grantor2")
                .account(writeAccount1)
                .authorization(Authorization.WRITE)
                .build();

        PowerOfAttorney poaRead2 = PowerOfAttorney.builder()
                .granteeName(GRANTEE_NAME)
                .grantorName("Grantor3")
                .account(readAccount2)
                .authorization(Authorization.READ)
                .build();

        List<PowerOfAttorney> mockPoaList = Arrays.asList(poaRead1, poaWrite1, poaRead2);

        when(powerOfAttorneyDataService.findAllPOAByGranteeName(GRANTEE_NAME))
                .thenReturn(mockPoaList);

        AccesibleAccountResponse response = accountApiService.getAccessibleAccountsByAuthType(GRANTEE_NAME);

        assertNotNull(response);
        assertFalse(response.accountsWithRead().isEmpty());
        assertFalse(response.accountsWithWrite().isEmpty());

        assertEquals(2, response.accountsWithRead().size());
        assertEquals(1, response.accountsWithWrite().size());

        assertTrue(response.accountsWithRead().contains(readAccount1));
        assertTrue(response.accountsWithRead().contains(readAccount2));
        assertFalse(response.accountsWithRead().contains(writeAccount1));

        assertTrue(response.accountsWithWrite().contains(writeAccount1));
        assertFalse(response.accountsWithWrite().contains(readAccount1));

        verify(powerOfAttorneyDataService, times(1)).findAllPOAByGranteeName(GRANTEE_NAME);
        verifyNoMoreInteractions(powerOfAttorneyDataService);
    }

    @Test
    void getAccessibleAccounts_shouldReturnEmptyLists_whenNoAuthorizationsFound() {
        when(powerOfAttorneyDataService.findAllPOAByGranteeName(GRANTEE_NAME))
                .thenReturn(Collections.emptyList());

        AccesibleAccountResponse response = accountApiService.getAccessibleAccountsByAuthType(GRANTEE_NAME);

        assertNotNull(response);
        assertTrue(response.accountsWithRead().isEmpty());
        assertTrue(response.accountsWithWrite().isEmpty());

        verify(powerOfAttorneyDataService, times(1)).findAllPOAByGranteeName(GRANTEE_NAME);
        verifyNoMoreInteractions(powerOfAttorneyDataService);
    }

    @Test
    void getAccessibleAccounts_shouldReturnOnlyReadAccounts_whenOnlyReadAuthorizationsExist() {
        Account readAccount = new PaymentAccount(ACCOUNT_NUMBER_READ, "Holder1", 100.0);
        PowerOfAttorney poaRead = PowerOfAttorney.builder()
                .granteeName(GRANTEE_NAME)
                .grantorName("Grantor1")
                .account(readAccount)
                .authorization(Authorization.READ)
                .build();

        when(powerOfAttorneyDataService.findAllPOAByGranteeName(GRANTEE_NAME))
                .thenReturn(Collections.singletonList(poaRead));

        AccesibleAccountResponse response = accountApiService.getAccessibleAccountsByAuthType(GRANTEE_NAME);

        assertNotNull(response);
        assertFalse(response.accountsWithRead().isEmpty());
        assertTrue(response.accountsWithWrite().isEmpty());

        assertEquals(1, response.accountsWithRead().size());
        assertTrue(response.accountsWithRead().contains(readAccount));

        verify(powerOfAttorneyDataService, times(1)).findAllPOAByGranteeName(GRANTEE_NAME);
    }

    @Test
    void getAccessibleAccounts_shouldReturnOnlyWriteAccounts_whenOnlyWriteAuthorizationsExist() {
        Account writeAccount = new SavingsAccount(ACCOUNT_NUMBER_WRITE, "Holder2", 200.0);
        PowerOfAttorney poaWrite = PowerOfAttorney.builder()
                .granteeName(GRANTEE_NAME)
                .grantorName("Grantor2")
                .account(writeAccount)
                .authorization(Authorization.WRITE)
                .build();

        when(powerOfAttorneyDataService.findAllPOAByGranteeName(GRANTEE_NAME))
                .thenReturn(Collections.singletonList(poaWrite));

        AccesibleAccountResponse response = accountApiService.getAccessibleAccountsByAuthType(GRANTEE_NAME);

        assertNotNull(response);
        assertTrue(response.accountsWithRead().isEmpty());
        assertFalse(response.accountsWithWrite().isEmpty());

        assertEquals(1, response.accountsWithWrite().size());
        assertTrue(response.accountsWithWrite().contains(writeAccount));

        verify(powerOfAttorneyDataService, times(1)).findAllPOAByGranteeName(GRANTEE_NAME);
    }
}