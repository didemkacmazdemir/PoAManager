package nl.rabobank.mongo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.mongo.document.PowerOfAttorneyDocument;
import nl.rabobank.mongo.mapper.PowerOfAttorneyMapper;
import nl.rabobank.mongo.model.exception.DuplicateAuthorizationException;
import nl.rabobank.mongo.repository.PowerOfAttorneyRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PowerOfAttorneyDataService {
    private final PowerOfAttorneyRepository powerOfAttorneyRepository;

    @Transactional
    public void savePowerOfAttorney(PowerOfAttorney powerOfAttorney) {
        PowerOfAttorneyDocument powerOfAttorneyDocument = PowerOfAttorneyMapper.INSTANCE.toPowerOfAttorneyDocument(powerOfAttorney);
        log.debug("powerOfAttorney -> powerOfAttorneyDocument conversion completed successfully");
        powerOfAttorneyRepository.save(powerOfAttorneyDocument);
    }

    @Transactional(readOnly = true)
    public void checkPOAExistsByGrantorNameAndGranteeNameAndAccountNumber(String grantorName, String granteeName, String accountNumber) {
        boolean powerOfAttorney = powerOfAttorneyRepository.existsByGrantorNameAndGranteeNameAndAccountAccountNumber(
                grantorName,
                granteeName,
                accountNumber);

        if (powerOfAttorney) {
            log.warn("Duplicate authorization attempt - Grantor: {}, Grantee: {}, Account: {}",
                    grantorName, granteeName, accountNumber);
            throw new DuplicateAuthorizationException("Authorization already exists");
        }
    }

    @Transactional(readOnly = true)
    public List<PowerOfAttorney> findAllPOAByGranteeName(String granteeName) {
        List<PowerOfAttorneyDocument> granteeNames = powerOfAttorneyRepository
                .findAllByGranteeName(granteeName);

        if (granteeNames.isEmpty()) {
            log.info("Poa can not be found for {}", granteeName);
            return Collections.emptyList();
        }
        return PowerOfAttorneyMapper.INSTANCE.toPowerOfAttorneyList(granteeNames);
    }
}
