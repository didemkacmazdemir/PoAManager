package nl.rabobank.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import nl.rabobank.model.response.AccesibleAccountResponse;
import nl.rabobank.service.AccountApiService;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
@Tag(name = "Accounts", description = "API for managing accounts")
public class AccountController {
    private final AccountApiService accountApiService;

    @Operation(summary = "Get accessible accounts",
            description = "Retrieves all accounts accessible by the grantee")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully retrieved accessible accounts",
                    content = @Content(schema = @Schema(implementation = AccesibleAccountResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid grantee name",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{granteeName}")
    @ResponseStatus(HttpStatus.OK)
    public AccesibleAccountResponse getAuthorizations(@PathVariable @NotBlank(message = "Grantee Name must not be blank") String granteeName) {
        return accountApiService.getAccessibleAccountsByAuthType(granteeName);
    }
}
