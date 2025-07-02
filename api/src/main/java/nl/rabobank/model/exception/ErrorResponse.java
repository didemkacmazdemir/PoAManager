package nl.rabobank.model.exception;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        String message,

        HttpStatus httpStatus,
        List<String> errors,
        LocalDateTime timestamp
) {}
