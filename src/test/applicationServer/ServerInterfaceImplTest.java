package applicationServer;

import client.GameInfo;
import client.UserInfo;
import databaseServer.DatabaseImpl;
import databaseServer.DbInterface;
import exceptions.GameFullException;
import exceptions.UsernameAlreadyUsedException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

public class ServerInterfaceImplTest {

    private Registry registry;
    private DbInterface dbInterface;
    private int DBPORTNUMBER = 1300;

    @Before
    public void init() {
        createDbServers(1300);
    }
    @Test(expected = UsernameAlreadyUsedException.class)
    public void registerTest_expectToken() throws RemoteException {
        createDbServers(DBPORTNUMBER);
        ServerInterface serverInterface = new ServerInterfaceImpl(1200, DBPORTNUMBER);
        String token = serverInterface.register("PindaKaas", "aPassword");
        System.out.println(token);
    }

    @Test
    public void loginTest_expectToken() throws RemoteException {
        DbInterface dbInterface = createDbServers(DBPORTNUMBER);
        ServerInterface serverInterface = new ServerInterfaceImpl(1200, DBPORTNUMBER);
        String token = serverInterface.login("PindaKaas", "aPassword");
        Assert.assertTrue(token != null && !token.isEmpty());
    }

    @Test
    public void addGame_expectID() throws RemoteException {
        ServerInterface serverInterface = new ServerInterfaceImpl(1200, DBPORTNUMBER);
        String game_id = serverInterface.startNewGame(createNewGame());
        Assert.assertTrue(game_id != null && !game_id.isEmpty());
    }

    @Test
    public void joinGame_expectNoError() throws RemoteException, GameFullException {
        ServerInterface serverInterface = new ServerInterfaceImpl(1200, DBPORTNUMBER);
        String game_id = serverInterface.startNewGame(createNewGame());
        System.out.println(game_id);

        serverInterface.joinGame(null, game_id, createNewUser("username"));
        serverInterface.joinGame(null, game_id, createNewUser("username"));
    }

    @Test(expected = GameFullException.class)
    public void joinGame_expectGameFullError() throws RemoteException, GameFullException {
        ServerInterface serverInterface = new ServerInterfaceImpl(1200, DBPORTNUMBER);
        String game_id = serverInterface.startNewGame(createNewGame());
        System.out.println(game_id);

        serverInterface.joinGame(null, game_id, createNewUser("username"));
        serverInterface.joinGame(null, game_id, createNewUser("username"));
        boolean bool = serverInterface.joinGame(null, game_id, createNewUser("username"));
    }

    @Test
    public void createGame_playWithBots() throws RemoteException {
        ServerInterface serverInterface = new ServerInterfaceImpl(1200, DBPORTNUMBER);
        String game_id = serverInterface.startNewGame(createNewGame());
        serverInterface.joinGameAddBot(game_id);
        serverInterface.joinGameAddBot(game_id);
    }

    @Test
    public void createGame_playWithBots_checkIfInactive() throws RemoteException {
        ServerInterface serverInterface = new ServerInterfaceImpl(1200, DBPORTNUMBER);
        String game_id = serverInterface.startNewGame(createNewGame());
        serverInterface.joinGameAddBot(game_id);
        serverInterface.joinGameAddBot(game_id);
        Assert.assertFalse(serverInterface.getGames().stream()
                .anyMatch(gameInfo -> gameInfo.getGameID() == game_id));
    }

    private DbInterface createDbServers(int portnumber) {
        registry = null;
        dbInterface = null;
        try {
            registry = LocateRegistry.createRegistry(portnumber);
            dbInterface = new DatabaseImpl(portnumber);
            registry.bind("UNOdatabase" + (portnumber), dbInterface);
        } catch (RemoteException | AlreadyBoundException e) {
            e.printStackTrace();
        }
        return dbInterface;
    }

    private GameInfo createNewGame() {
        return new GameInfo.Builder()
                .setGameID(UUID.randomUUID().toString())
                .setGameName("myNewGame")
                .setAmountOfPlayers(2)
                .setGameTheme(1)
                .build();
    }

    private UserInfo createNewUser(String username) {
        return new UserInfo.InnerBuilder()
                .setName(username)
                .buildUserInfo();
    }
}
