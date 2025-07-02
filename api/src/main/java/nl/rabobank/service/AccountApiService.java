package nl.rabobank.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.account.Account;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.authorizations.PowerOfAttorney;
import nl.rabobank.model.response.AccesibleAccountResponse;
import nl.rabobank.mongo.service.PowerOfAttorneyDataService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountApiService {
    private final PowerOfAttorneyDataService powerOfAttorneyDataService;

    public AccesibleAccountResponse getAccessibleAccountsByAuthType(String granteeName) {
        Map<Authorization, List<Account>> poaAccountsByAuthorisation = powerOfAttorneyDataService
                .findAllPOAByGranteeName(granteeName)
                .stream()
                .collect(Collectors.groupingBy(
                        PowerOfAttorney::getAuthorization,
                        Collectors.mapping(PowerOfAttorney::getAccount, Collectors.toList())
                ));

        if (poaAccountsByAuthorisation.isEmpty()) {
            log.info("No authorizations found for grantee: {}", granteeName);
            return new AccesibleAccountResponse(Collections.emptyList(), Collections.emptyList());
        }

        return new AccesibleAccountResponse(
                poaAccountsByAuthorisation.getOrDefault(Authorization.READ, Collections.emptyList()),
                poaAccountsByAuthorisation.getOrDefault(Authorization.WRITE, Collections.emptyList())
        );
    }

}
