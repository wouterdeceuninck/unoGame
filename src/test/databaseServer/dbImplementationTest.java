package databaseServer;

import org.junit.Test;

import java.rmi.RemoteException;

public class dbImplementationTest {

    DbInterface dbInterface;

    @Test
    public void init() throws RemoteException {
        dbInterface = new DatabaseImpl(1300);
    }

}
