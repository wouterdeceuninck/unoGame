package exceptions;

public class CardIsNotPresentException extends Throwable {
    public CardIsNotPresentException(String s) {
        super(s);
    }
}
