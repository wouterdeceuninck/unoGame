package exceptions;

public class FailedToLoginException extends RuntimeException {
        public FailedToLoginException(final String message) {
            super(message);
        }
    }