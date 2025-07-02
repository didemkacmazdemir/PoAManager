package nl.rabobank.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.rabobank.model.request.PowerOfAttorneyRequest;
import nl.rabobank.model.response.PowerOfAttorneyResponse;
import nl.rabobank.service.PowerAttorneyApiService;
import org.springframework.http.HttpStatus;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/power-of-attorney")
@Tag(name = "Power of Attorney", description = "API for managing power of attorney authorizations")
public class PowerOfAttorneyController {

    private final PowerAttorneyApiService powerAttorneyApiService;

    @Operation(summary = "Create a new power of attorney", description = "Creates a new power of attorney authorization")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Power of attorney created successfully",
                    content = @Content(schema = @Schema(implementation = PowerOfAttorneyResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Account not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PowerOfAttorneyResponse createPowerOfAttorney(
            @Valid @RequestBody PowerOfAttorneyRequest powerOfAttorneyRequest) {
        return powerAttorneyApiService.createPowerOfAttorney(powerOfAttorneyRequest);
    }

}
