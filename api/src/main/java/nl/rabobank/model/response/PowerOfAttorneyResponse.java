package nl.rabobank.model.response;

import nl.rabobank.authorizations.Authorization;

public record PowerOfAttorneyResponse(String grantorName, String accountNumber, Authorization authorization) {
}
