package  exceptions;

public class UnAutherizedException extends RuntimeException {
    public UnAutherizedException(String message) {
        super(message);
    }
}