package  exceptions;

import java.io.Serializable;

public class GameFullException extends RuntimeException implements Serializable {
    public GameFullException(String s) {
        super(s);
    }
}
