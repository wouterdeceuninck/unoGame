package  exceptions;

public class UsernameAlreadyUsedException extends RuntimeException {
    public UsernameAlreadyUsedException (String message) {
        super(message);
    }
}
