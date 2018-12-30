package exceptions;

import java.io.Serializable;

public class GameFullException extends RuntimeException{
    public GameFullException(String s) {
        super(s);
    }
}
