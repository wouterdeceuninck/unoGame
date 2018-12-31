package exceptions;

public class RerouteNeededExeption extends RuntimeException{
    public RerouteNeededExeption(int portnumber) {
        super(String.valueOf(portnumber));
    }
}
