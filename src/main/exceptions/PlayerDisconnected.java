package exceptions;

public class PlayerDisconnected extends RuntimeException {
    public PlayerDisconnected(String s) {
        super(s);
    }
}
