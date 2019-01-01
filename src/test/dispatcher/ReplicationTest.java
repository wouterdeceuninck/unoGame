package dispatcher;

import applicationServer.ServerInterface;
import client.GameInfo;
import databaseServer.DbInterface;
import org.junit.Assert;
import org.junit.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ReplicationTest {

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

    @Test
    public void createDbServers_replicateLogin() throws RemoteException, InterruptedException {
        new Main().startServer();
        DispatcherInterface dispatcherInterface = connectToDispatcher();
        int dbServer = dispatcherInterface.createDBServer();
        int dbServer1 = dispatcherInterface.createDBServer();

        DbInterface dbInterface1 = connectToDbServer(dbServer);
        DbInterface dbInterface2 = connectToDbServer(dbServer1);

        ServerInterface serverInterface = connectToApplicationServer(dispatcherInterface.createApplicationServer());
        for (int i = 0; i< 1000; i++) {
            testLogin(dbInterface1, dbInterface2, serverInterface);
        }
    }

    @Test
    public void createDbServers_replicateLogin_4servers() throws RemoteException, InterruptedException {
        new Main().startServer();
        DispatcherInterface dispatcherInterface = connectToDispatcher();

        DbInterface dbInterface1 = connectToDbServer(dispatcherInterface.createDBServer());
        DbInterface dbInterface2 = connectToDbServer(dispatcherInterface.createDBServer());
        DbInterface dbInterface3 = connectToDbServer(dispatcherInterface.createDBServer());
        DbInterface dbInterface4 = connectToDbServer(dispatcherInterface.createDBServer());

        List<ServerInterface> allApplicationServers = new ArrayList<>();
        allApplicationServers.add(connectToApplicationServer(dispatcherInterface.createApplicationServer()));
        allApplicationServers.add(connectToApplicationServer(dispatcherInterface.createApplicationServer()));
        allApplicationServers.add(connectToApplicationServer(dispatcherInterface.createApplicationServer()));
        allApplicationServers.add(connectToApplicationServer(dispatcherInterface.createApplicationServer()));

        allApplicationServers.get(0).register("PindaKaas", "aPassword");
        for (int i = 0; i< 10; i++) {
            for (ServerInterface applicationServer : allApplicationServers) {
                applicationServer.login("PindaKaas", "aPassword");
                Thread.sleep(10);

                String pindaKaas1 = dbInterface1.getUser_forTestsOnly("PindaKaas");
                String pindaKaas2 = dbInterface2.getUser_forTestsOnly("PindaKaas");
                String pindaKaas3 = dbInterface3.getUser_forTestsOnly("PindaKaas");
                String pindaKaas4 = dbInterface4.getUser_forTestsOnly("PindaKaas");

                System.out.println(pindaKaas1);
                System.out.println(pindaKaas2);
                System.out.println(pindaKaas3);
                System.out.println(pindaKaas4);
                System.out.println("\n");

                Assert.assertEquals(pindaKaas1, pindaKaas2);
                Assert.assertEquals(pindaKaas1, pindaKaas3);
                Assert.assertEquals(pindaKaas1, pindaKaas4);
                Assert.assertEquals(pindaKaas2, pindaKaas3);
                Assert.assertEquals(pindaKaas2, pindaKaas4);
                Assert.assertEquals(pindaKaas3, pindaKaas4);
            }
        }
    }

    @Test
    public void createDbServers_replicateAddGame() throws RemoteException, InterruptedException {
        new Main().startServer();
        DispatcherInterface dispatcherInterface = connectToDispatcher();

        int dbServer = dispatcherInterface.createDBServer();
        int dbServer1 = dispatcherInterface.createDBServer();

        System.out.println(dbServer);
        System.out.println(dbServer1);

        DbInterface dbInterface1 = connectToDbServer(dbServer);
        DbInterface dbInterface2 = connectToDbServer(dbServer1);

        ServerInterface serverInterface = connectToApplicationServer(dispatcherInterface.createApplicationServer());
        serverInterface.startNewGame(this.createNewGame());
        Thread.sleep(100);

        List<GameInfo> activeGames1 = dbInterface1.getActiveGames();
        List<GameInfo> activeGames2 = dbInterface2.getActiveGames();

        System.out.println(activeGames1.size());
        System.out.println(activeGames2.size());

        Assert.assertEquals(activeGames1.size(), activeGames2.size());
    }

    @Test
    public void createDbServers_replicateAddGame_4servers() throws RemoteException, InterruptedException {
        new Main().startServer();
        DispatcherInterface dispatcherInterface = connectToDispatcher();

        DbInterface dbInterface1 = connectToDbServer(dispatcherInterface.createDBServer());
        DbInterface dbInterface2 = connectToDbServer(dispatcherInterface.createDBServer());
        DbInterface dbInterface3 = connectToDbServer(dispatcherInterface.createDBServer());
        DbInterface dbInterface4 = connectToDbServer(dispatcherInterface.createDBServer());

        ServerInterface serverInterface = connectToApplicationServer(dispatcherInterface.createApplicationServer());
        serverInterface.startNewGame(this.createNewGame());
        Thread.sleep(100);

        List<GameInfo> activeGames1 = dbInterface1.getActiveGames();
        List<GameInfo> activeGames2 = dbInterface2.getActiveGames();
        List<GameInfo> activeGames3 = dbInterface3.getActiveGames();
        List<GameInfo> activeGames4 = dbInterface4.getActiveGames();

        System.out.println(activeGames1.size());
        System.out.println(activeGames2.size());
        System.out.println(activeGames3.size());
        System.out.println(activeGames4.size());

        Assert.assertEquals(activeGames1.size(), activeGames2.size());
        Assert.assertEquals(activeGames1.size(), activeGames3.size());
        Assert.assertEquals(activeGames1.size(), activeGames4.size());
        Assert.assertEquals(activeGames2.size(), activeGames3.size());
        Assert.assertEquals(activeGames2.size(), activeGames4.size());
        Assert.assertEquals(activeGames3.size(), activeGames4.size());
    }

    @Test
    public void createDBServers_replicateJoinGame() throws RemoteException, InterruptedException {
        new Main().startServer();
        DispatcherInterface dispatcherInterface = connectToDispatcher();

        DbInterface dbInterface1 = connectToDbServer(dispatcherInterface.createDBServer());
        DbInterface dbInterface2 = connectToDbServer(dispatcherInterface.createDBServer());

        ServerInterface serverInterface = connectToApplicationServer(dispatcherInterface.createApplicationServer());
        String portnumberAndGameId = serverInterface.startNewGame(this.createNewGame());

        String gameID = portnumberAndGameId.split("_")[1];

        serverInterface.joinGame(null, portnumberAndGameId, "myUsername");
        Thread.sleep(100);

        List<GameInfo> activeGames1 = dbInterface1.getActiveGames();
        List<GameInfo> activeGames2 = dbInterface2.getActiveGames();

        int connectedPlayers1 = activeGames1.stream()
                .filter(gameInfo -> gameInfo.getGameID().equals(gameID))
                .findFirst()
                .get()
                .getConnectedPlayers();

        int connectedPlayers2 = activeGames2.stream()
                .filter(gameInfo -> gameInfo.getGameID().equals(gameID))
                .findFirst()
                .get()
                .getConnectedPlayers();

        System.out.println(connectedPlayers1);
        System.out.println(connectedPlayers2);

        Assert.assertEquals(connectedPlayers1, connectedPlayers2);
    }

    private void testLogin(DbInterface dbInterface1, DbInterface dbInterface2, ServerInterface serverInterface) throws RemoteException, InterruptedException {
        serverInterface.login("PindaKaas", "aPassword");
        Thread.sleep(10);

        String pindaKaas1 = dbInterface1.getUser_forTestsOnly("PindaKaas");
        String pindaKaas2 = dbInterface2.getUser_forTestsOnly("PindaKaas");

        System.out.println(pindaKaas1);
        System.out.println(pindaKaas2);
        System.out.println("\n");

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
