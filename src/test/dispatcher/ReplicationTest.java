package dispatcher;

import applicationServer.ServerInterface;
import client.GameInfo;
import databaseServer.DbInterface;
import org.junit.Assert;
import org.junit.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.UUID;

public class ReplicationTest {

    @Test
    public void createDbServers_replicateLogin() throws RemoteException, InterruptedException {
        new Main().startServer();
        DispatcherInterface dispatcherInterface = connectToDispatcher();
        int dbServer = dispatcherInterface.createDBServer();
        int dbServer1 = dispatcherInterface.createDBServer();

        DbInterface dbInterface1 = connectToDbServer(dbServer);
        DbInterface dbInterface2 = connectToDbServer(dbServer1);

        ServerInterface serverInterface = connectToApplicationServer(dispatcherInterface.createApplicationServer());
        serverInterface.login("PindaKaas", "aPassword");
        Thread.sleep(100);

        String pindaKaas1 = dbInterface1.getUser_forTestsOnly("PindaKaas");
        String pindaKaas2 = dbInterface2.getUser_forTestsOnly("PindaKaas");

        System.out.println(pindaKaas1);
        System.out.println(pindaKaas2);
    }

    @Test
    public void createDbServers_replicateRegister() throws RemoteException, InterruptedException {
        new Main().startServer();
        DispatcherInterface dispatcherInterface = connectToDispatcher();
        int dbServer = dispatcherInterface.createDBServer();
        int dbServer1 = dispatcherInterface.createDBServer();

        DbInterface dbInterface1 = connectToDbServer(dbServer);
        DbInterface dbInterface2 = connectToDbServer(dbServer1);

        ServerInterface serverInterface = connectToApplicationServer(dispatcherInterface.createApplicationServer());
        serverInterface.register("PindaKaas", "aPassword");
        Thread.sleep(100);

        String pindaKaas1 = dbInterface1.getUser_forTestsOnly("PindaKaas");
        String pindaKaas2 = dbInterface2.getUser_forTestsOnly("PindaKaas");

        System.out.println(pindaKaas1);
        System.out.println(pindaKaas2);

        Assert.assertEquals(pindaKaas1, pindaKaas2);
    }

    private ServerInterface connectToApplicationServer(int portnumber) {
        try {
            return (ServerInterface) LocateRegistry.getRegistry("localhost", portnumber).lookup("UNOserver");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private DispatcherInterface connectToDispatcher() {
        try {
            return (DispatcherInterface) LocateRegistry.getRegistry("localhost", 1099).lookup("UNOdispatcher");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private DbInterface connectToDbServer(int portnumber) {
        try {
            return (DbInterface) LocateRegistry.getRegistry("localhost", portnumber).lookup("UNOdatabase" + portnumber);
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private GameInfo createNewGame() {
        return new GameInfo.Builder()
                .setGameID(UUID.randomUUID().toString())
                .setGameName("myNewGame")
                .setAmountOfPlayers(2)
                .setGameTheme(1)
                .build();
    }
}
