package nl.rabobank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.rabobank.authorizations.Authorization;
import nl.rabobank.model.request.PowerOfAttorneyRequest;
import nl.rabobank.mongo.repository.PowerOfAttorneyRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.hasItem; // hasItem için bu importu ekleyin

@SpringBootTest
@AutoConfigureMockMvc
class PowerOfAttorneyDocumentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    PowerOfAttorneyRepository powerOfAttorneyRepository;

    private static final String VALID_ACCOUNT_NUMBER = "NL91RABO0123456789";

    @AfterEach
    void setup() {
        powerOfAttorneyRepository.deleteAll();
    }

    @Test
    void createPowerOfAttorney_shouldReturnCreated_whenRequestIsValid() throws Exception {
        PowerOfAttorneyRequest request = new PowerOfAttorneyRequest(
                "grantor123",
                "grantee456",
                Authorization.READ,
                VALID_ACCOUNT_NUMBER
        );

        mockMvc.perform(post("/api/v1/power-of-attorney")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidRequests")
    void createPowerOfAttorney_shouldReturnBadRequest_forInvalidRequests(PowerOfAttorneyRequest request, String expectedErrorMessage) throws Exception {
        mockMvc.perform(post("/api/v1/power-of-attorney")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasItem(expectedErrorMessage)));
    }

    static Stream<Arguments> provideInvalidRequests() {
        StringBuilder sb = new StringBuilder("try");
        for (int i = 0; i < 98; i++) {
            sb.append("a");
        }
        return Stream.of(
                Arguments.of(
                        new PowerOfAttorneyRequest("grantor123", "grantor123", Authorization.READ, VALID_ACCOUNT_NUMBER),
                        "Grantor and grantee cannot be the same"
                ),
                Arguments.of(
                        new PowerOfAttorneyRequest("grantor123", "grantee456", Authorization.READ, ""),
                        "Account number must be specified"
                ),
                Arguments.of(
                        new PowerOfAttorneyRequest("", "grantor123", Authorization.READ, VALID_ACCOUNT_NUMBER),
                        "Grantor Name cannot be blank"
                ),
                Arguments.of(
                        new PowerOfAttorneyRequest("grantor123", "", Authorization.READ, VALID_ACCOUNT_NUMBER),
                        "Grantee Name cannot be blank"
                ),
                Arguments.of(
                        new PowerOfAttorneyRequest(sb.toString(), "grantor123", Authorization.READ, VALID_ACCOUNT_NUMBER),
                        "Grantor Name must be less than 100 characters"
                ),

                Arguments.of(
                        new PowerOfAttorneyRequest("grantor123", sb.toString(), Authorization.READ, VALID_ACCOUNT_NUMBER),
                        "Grantee Name must be less than 100 characters"
                )
        );
    }
}