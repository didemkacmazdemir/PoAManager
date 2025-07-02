package nl.rabobank.mongo.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.account.Account;
import nl.rabobank.mongo.document.AccountDocument;
import nl.rabobank.mongo.mapper.AccountMapper;
import nl.rabobank.mongo.model.exception.AccountNotFoundException;
import nl.rabobank.mongo.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountDataService {
    private final AccountRepository accountRepository;

    @Transactional(readOnly = true)
    public Account getAccountByAccountNumber(String accountNumber) throws AccountNotFoundException {
        AccountDocument accountDocument = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> {
            log.error("Account not found: {}", accountNumber);
            return new AccountNotFoundException("Account with number " + accountNumber + " not found");
        });
        return AccountMapper.INSTANCE.toAccount(accountDocument);
    }

}
