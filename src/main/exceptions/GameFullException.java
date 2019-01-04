package  exceptions;

import java.io.Serializable;

public class GameFullException extends Throwable implements Serializable {
    public GameFullException(String s) {
        super(s);
    }
}
