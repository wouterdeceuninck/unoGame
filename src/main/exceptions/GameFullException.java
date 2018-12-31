package  exceptions;

public class GameFullException extends RuntimeException{
    public GameFullException(String s) {
        super(s);
    }
}
