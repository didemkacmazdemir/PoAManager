package nl.rabobank.mongo.model.exception;

public class DuplicateAuthorizationException extends RuntimeException {
    public DuplicateAuthorizationException(String message) {
        super(message);
    }
}