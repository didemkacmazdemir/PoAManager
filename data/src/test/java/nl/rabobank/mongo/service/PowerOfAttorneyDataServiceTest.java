package nl.rabobank.mongo.service;

import nl.rabobank.account.Account;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.mongo.document.PowerOfAttorneyDocument;
import nl.rabobank.mongo.mapper.PowerOfAttorneyMapper;
import nl.rabobank.mongo.model.exception.DuplicateAuthorizationException;
import nl.rabobank.mongo.repository.PowerOfAttorneyRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PowerOfAttorneyDataServiceTest {
    @Mock
    private PowerOfAttorneyRepository powerOfAttorneyRepository;

    @Mock
    private PowerOfAttorneyMapper powerOfAttorneyMapper;

    @InjectMocks
    private PowerOfAttorneyDataService powerOfAttorneyDataService;

    @Test
    void save_shouldConvertAndSaveDocument() {
        PowerOfAttorney powerOfAttorney = PowerOfAttorney.builder()
                .grantorName("grantor")
                .granteeName("grantee")
                .authorization(Authorization.READ)
                .account(mock(Account.class))
                .build();

        when(powerOfAttorneyRepository.save(any(PowerOfAttorneyDocument.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        powerOfAttorneyDataService.savePowerOfAttorney(powerOfAttorney);

        verify(powerOfAttorneyRepository).save(argThat(doc ->
                doc.getGrantorName().equals("grantor") &&
                        doc.getGranteeName().equals("grantee") &&
                        doc.getAuthorization() == Authorization.READ
        ));
    }


    @Test
    void save_shouldLogConversionSuccess() {
        PowerOfAttorney powerOfAttorney = PowerOfAttorney.builder()
                .grantorName("Alice")
                .granteeName("Bob")
                .authorization(Authorization.WRITE)
                .account(mock(Account.class))
                .build();

        powerOfAttorneyDataService.savePowerOfAttorney(powerOfAttorney);

        verify(powerOfAttorneyRepository).save(any(PowerOfAttorneyDocument.class));
    }

    @Test
    void checkPOAExists_shouldThrowExceptionWhenDuplicateExists() {
        String grantorName = "grantor";
        String granteeName = "grantee";
        String accountNumber = "123456";

        when(powerOfAttorneyRepository.existsByGrantorNameAndGranteeNameAndAccountAccountNumber(
                grantorName, granteeName, accountNumber))
                .thenReturn(true);

        DuplicateAuthorizationException exception = assertThrows(DuplicateAuthorizationException.class,
                () -> powerOfAttorneyDataService.checkPOAExistsByGrantorNameAndGranteeNameAndAccountNumber(
                        grantorName, granteeName, accountNumber));

        assertEquals("Authorization already exists", exception.getMessage());
        verify(powerOfAttorneyRepository).existsByGrantorNameAndGranteeNameAndAccountAccountNumber(
                grantorName, granteeName, accountNumber);
    }

    @Test
    void checkPOAExists_shouldNotThrowExceptionWhenNoDuplicate() {
        String grantorName = "grantor";
        String granteeName = "grantee";
        String accountNumber = "123456";

        when(powerOfAttorneyRepository.existsByGrantorNameAndGranteeNameAndAccountAccountNumber(
                grantorName, granteeName, accountNumber))
                .thenReturn(false);

        assertDoesNotThrow(() ->
                powerOfAttorneyDataService.checkPOAExistsByGrantorNameAndGranteeNameAndAccountNumber(
                        grantorName, granteeName, accountNumber));

        verify(powerOfAttorneyRepository).existsByGrantorNameAndGranteeNameAndAccountAccountNumber(
                grantorName, granteeName, accountNumber);
    }

    @Test
    void findAllPOAByGranteeName_shouldReturnEmptyListWhenNoResults() {
        String granteeName = "nonexistent";
        when(powerOfAttorneyRepository.findAllByGranteeName(granteeName))
                .thenReturn(Collections.emptyList());

        List<PowerOfAttorney> result = powerOfAttorneyDataService.findAllPOAByGranteeName(granteeName);

        assertTrue(result.isEmpty());
        verify(powerOfAttorneyRepository).findAllByGranteeName(granteeName);
    }

    @Test
    void findAllPOAByGranteeName_shouldReturnMappedResults() {
        String granteeName = "grantee";
        PowerOfAttorneyDocument doc1 = new PowerOfAttorneyDocument();
        doc1.setGranteeName(granteeName);
        doc1.setGrantorName("grantor1");
        doc1.setAuthorization(Authorization.READ);

        PowerOfAttorneyDocument doc2 = new PowerOfAttorneyDocument();
        doc2.setGranteeName(granteeName);
        doc2.setGrantorName("grantor2");
        doc2.setAuthorization(Authorization.WRITE);

        List<PowerOfAttorneyDocument> documents = Arrays.asList(doc1, doc2);

        when(powerOfAttorneyRepository.findAllByGranteeName(granteeName))
                .thenReturn(documents);

        List<PowerOfAttorney> result = powerOfAttorneyDataService.findAllPOAByGranteeName(granteeName);

        assertEquals(2, result.size());
        assertEquals("grantor1", result.get(0).getGrantorName());
        assertEquals(Authorization.READ, result.get(0).getAuthorization());
        assertEquals("grantor2", result.get(1).getGrantorName());
        assertEquals(Authorization.WRITE, result.get(1).getAuthorization());

        verify(powerOfAttorneyRepository).findAllByGranteeName(granteeName);
    }

}