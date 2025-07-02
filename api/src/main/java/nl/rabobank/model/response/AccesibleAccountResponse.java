package nl.rabobank.model.response;

import nl.rabobank.account.Account;

import java.util.List;

public record AccesibleAccountResponse(List<Account> accountsWithRead, List<Account> accountsWithWrite) {
}
