package nl.rabobank.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.account.Account;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.mapper.PowerOfAttorneyMapper;
import nl.rabobank.model.request.PowerOfAttorneyRequest;
import nl.rabobank.model.response.PowerOfAttorneyResponse;
import nl.rabobank.mongo.service.AccountDataService;
import nl.rabobank.mongo.service.PowerOfAttorneyDataService;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PowerAttorneyApiService {

    private final AccountDataService accountDataService;
    private final PowerOfAttorneyDataService powerOfAttorneyDataService;

    public PowerOfAttorneyResponse createPowerOfAttorney(PowerOfAttorneyRequest powerOfAttorneyRequest) {
        log.debug("Creating power of attorney for request: {}", powerOfAttorneyRequest);
        var accountNumber = powerOfAttorneyRequest.accountNumber();
        powerOfAttorneyDataService.checkPOAExistsByGrantorNameAndGranteeNameAndAccountNumber(powerOfAttorneyRequest.grantorName(),
                powerOfAttorneyRequest.granteeName(), accountNumber);

        var account = accountDataService.getAccountByAccountNumber(accountNumber);
        var powerOfAttorney = createPowerOfAttorneyWithAccount(powerOfAttorneyRequest, account);

        powerOfAttorneyDataService.savePowerOfAttorney(powerOfAttorney);
        log.info("Power of attorney created successfully for account {} and with {} auth",
                account.getAccountNumber(), powerOfAttorneyRequest.authorization());

        return PowerOfAttorneyMapper.INSTANCE.toDomainPowerOfAttorneyResponse(powerOfAttorney);
    }

    private PowerOfAttorney createPowerOfAttorneyWithAccount(PowerOfAttorneyRequest powerOfAttorneyRequest,
                                                             Account existingAccount) {
        return PowerOfAttorneyMapper.INSTANCE.toDomainPowerOfAttorney(powerOfAttorneyRequest)
                .toBuilder()
                .account(existingAccount)
                .build();
    }

}
