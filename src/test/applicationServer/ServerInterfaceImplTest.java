package applicationServer;

import client.GameInfo;
import databaseServer.DatabaseImpl;
import databaseServer.DbInterface;
import exceptions.GameFullException;
import exceptions.UsernameAlreadyUsedException;
import org.junit.Test;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

public class ServerInterfaceImplTest {

    @Test(expected = UsernameAlreadyUsedException.class)
    public void registerTest_expectToken() throws RemoteException {
        int dbPortnumber = 1300;
        createDbServers(dbPortnumber);
        ServerInterface serverInterface = new ServerInterfaceImpl(1200, dbPortnumber);
        String token = serverInterface.register("PindaKaas", "aPassword");
        System.out.println(token);
    }

    @Test
    public void loginTest_expectToken() throws RemoteException {
        int dbPortnumber = 1300;
        createDbServers(dbPortnumber);
        ServerInterface serverInterface = new ServerInterfaceImpl(1200, dbPortnumber);
        String token = serverInterface.login("PindaKaas", "aPassword");
        System.out.println(token);
    }

    @Test
    public void addGame_expectID() throws RemoteException {
        int dbPortnumber = 1300;
        createDbServers(dbPortnumber);
        ServerInterface serverInterface = new ServerInterfaceImpl(1200, dbPortnumber);
        String game_id = serverInterface.startNewGame(new GameInfo.Builder()
                .setGameID(UUID.randomUUID().toString())
                .setGameName("myNewGame")
                .setAmountOfPlayers(2)
                .setGameTheme(1)
                .build());
        System.out.println(game_id);
    }

    @Test
    public void joinGame_expectNoError() throws RemoteException, GameFullException {
        int dbPortnumber = 1300;
        createDbServers(dbPortnumber);
        ServerInterface serverInterface = new ServerInterfaceImpl(1200, dbPortnumber);
        String game_id = serverInterface.startNewGame(new GameInfo.Builder()
                .setGameID(UUID.randomUUID().toString())
                .setGameName("myNewGame")
                .setAmountOfPlayers(2)
                .setGameTheme(1)
                .build());
        System.out.println(game_id);

        serverInterface.joinGame(null, game_id, "username");
        serverInterface.joinGame(null, game_id, "username");
    }

    @Test(expected = GameFullException.class)
    public void joinGame_expectGameFullError() throws RemoteException, GameFullException {
        int dbPortnumber = 1300;
        createDbServers(dbPortnumber);
        ServerInterface serverInterface = new ServerInterfaceImpl(1200, dbPortnumber);
        String game_id = serverInterface.startNewGame(new GameInfo.Builder()
                .setGameID(UUID.randomUUID().toString())
                .setGameName("myNewGame")
                .setAmountOfPlayers(2)
                .setGameTheme(1)
                .build());
        System.out.println(game_id);

        serverInterface.joinGame(null, game_id, "username");
        serverInterface.joinGame(null, game_id, "username");
        boolean bool = serverInterface.joinGame(null, game_id, "username");
    }

    private void createDbServers(int portnumber) {
        Registry registry = null;
        try {
            registry = LocateRegistry.createRegistry(portnumber);
            DbInterface dbInterface = new DatabaseImpl(portnumber);
            registry.bind("UNOdatabase" + (portnumber), dbInterface);
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
    }
}
