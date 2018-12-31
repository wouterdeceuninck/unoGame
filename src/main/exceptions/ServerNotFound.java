package exceptions;

public class ServerNotFound extends Throwable {
    public ServerNotFound (String message) {
        super(message);
    }
}
