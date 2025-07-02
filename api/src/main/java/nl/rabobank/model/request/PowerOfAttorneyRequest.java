package nl.rabobank.model.request;

import jakarta.validation.constraints.*;
import nl.rabobank.authorizations.Authorization;

public record PowerOfAttorneyRequest(
        @NotBlank(message = "Grantor Name cannot be blank")
        @Size(max = 100, message = "Grantor Name must be less than 100 characters")
        String grantorName,

        @NotBlank(message = "Grantee Name cannot be blank")
        @Size(max = 100, message = "Grantee Name must be less than 100 characters")
        String granteeName,

        @NotNull(message = "Permission type must be specified")
        Authorization authorization,

        @NotBlank(message = "Account number must be specified")
        String accountNumber

) {
    @AssertTrue(message = "Grantor and grantee cannot be the same")
    public boolean isGrantorDifferentFromGrantee() {
        return !grantorName.equalsIgnoreCase(granteeName);
    }
}