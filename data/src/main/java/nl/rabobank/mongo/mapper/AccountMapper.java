package nl.rabobank.mongo.mapper;

import nl.rabobank.account.Account;
import nl.rabobank.account.PaymentAccount;
import nl.rabobank.account.SavingsAccount;
import nl.rabobank.mongo.document.AccountDocument;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    default Account toAccount(AccountDocument accountDocument) {
        if (accountDocument == null) return null;

        return switch (accountDocument.getAccountType()) {
            case PAYMENT ->
                    new PaymentAccount(accountDocument.getAccountNumber(), accountDocument.getAccountHolderName(), accountDocument.getBalance());
            case SAVINGS ->
                    new SavingsAccount(accountDocument.getAccountNumber(), accountDocument.getAccountHolderName(), accountDocument.getBalance());
        };
    }

}
