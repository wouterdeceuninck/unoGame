package dispatcher;

import org.junit.Test;
import org.mockito.cglib.proxy.Dispatcher;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class DispatcherInterfaceImplTest {

    @Test
    public void create1ApplicationServer() throws RemoteException {
        new Main().startServer(1);
        DispatcherInterface dispatcherInterface = connectToDispatcher();
        dispatcherInterface.createDBServer();
        dispatcherInterface.createApplicationServer();
        dispatcherInterface.createApplicationServer();
    }

    @Test
    public void create2ApplicationServers () {
        new Main().startServer(2);
    }

    @Test
    public void createGamesOnServers () {
        new Main().startServer(2);

    }

    private DispatcherInterface connectToDispatcher() {
        try {
            return (DispatcherInterface) LocateRegistry.getRegistry("localhost", 1099).lookup("UNOdispatcher");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
