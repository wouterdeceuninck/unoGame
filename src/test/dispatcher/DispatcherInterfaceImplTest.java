package dispatcher;

import applicationServer.ServerInterface;
import client.businessObjects.GameInfo;
import client.businessObjects.UserInfo;
import databaseServer.DbInterface;
import exceptions.GameFullException;
import exceptions.RerouteNeededExeption;
import org.junit.Assert;
import org.junit.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.UUID;

public class DispatcherInterfaceImplTest {

    @Test
    public void create1ApplicationServer() throws RemoteException {
        new Main().startServer();
        DispatcherInterface dispatcherInterface = connectToDispatcher();
        dispatcherInterface.createDBServer();
        System.out.println(dispatcherInterface.createApplicationServer());
    }

    @Test
    public void createGamesOnServers () throws RemoteException {
        new Main().startServer();
        DispatcherInterface dispatcherInterface = connectToDispatcher();
        dispatcherInterface.createDBServer();
        int portnumber = dispatcherInterface.createApplicationServer();
        System.out.println(portnumber);

        ServerInterface serverInterface = connectToApplicationServer(portnumber);
        String token = serverInterface.register(UUID.randomUUID().toString(), "aPassword");
        System.out.println(serverInterface.startNewGame(createNewGame(), token));
    }

    @Test
    public void createGamesOnServers_rerouteNeeded () throws RemoteException, GameFullException {
        new Main().startServer();
        DispatcherInterface dispatcherInterface = connectToDispatcher();
        dispatcherInterface.createDBServer();
        int portnumber = dispatcherInterface.createApplicationServer();
        System.out.println(portnumber);

        ServerInterface serverInterface = connectToApplicationServer(portnumber);
        String token = serverInterface.register(UUID.randomUUID().toString(), "aPassword");
        for (int i = 1; i<=21; i++) {
            System.out.println(i + ": " + serverInterface.startNewGame(createNewGame(), token));
        }
        String ID = serverInterface.startNewGame(createNewGame(), token);
        System.out.println(ID);
        try{
            serverInterface.joinGame(null, ID, token);
        } catch (RerouteNeededExeption rerouteNeededExeption) {
            serverInterface = connectToApplicationServer(Integer.parseInt(rerouteNeededExeption.getMessage()));
        }
        Assert.assertTrue(serverInterface.joinGame(null, ID, token));
    }

    @Test
    public void createMultipleDbServers() throws RemoteException {
        new Main().startServer();
        DispatcherInterface dispatcherInterface = connectToDispatcher();
        System.out.println(dispatcherInterface.createDBServer());
        System.out.println(dispatcherInterface.createDBServer());
    }

    @Test
    public void createMultipleDbServers_getLeastLoaded() throws RemoteException {
        new Main().startServer();
        DispatcherInterface dispatcherInterface = connectToDispatcher();
        System.out.println(dispatcherInterface.createDBServer());
        System.out.println(dispatcherInterface.createDBServer());
        dispatcherInterface.getDBServerPortnumber();
        dispatcherInterface.createApplicationServer();
        dispatcherInterface.getDBServerPortnumber();
    }

    private GameInfo createNewGame() {
        return new GameInfo.Builder()
                .setGameID(UUID.randomUUID().toString())
                .setGameName("myNewGame")
                .setAmountOfPlayers(2)
                .setGameTheme(1)
                .build();
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
            return (DbInterface) LocateRegistry.getRegistry("localhost", portnumber).lookup("UNOdatabase");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private UserInfo createNewUser(String username) {
        return new UserInfo.InnerBuilder()
                .setName(username)
                .buildUserInfo();
    }
}
