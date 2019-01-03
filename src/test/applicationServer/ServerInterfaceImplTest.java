package applicationServer;

import client.businessObjects.GameInfo;
import client.businessObjects.UserInfo;
import databaseServer.DatabaseImpl;
import databaseServer.DbInterface;
import dispatcher.Main;
import exceptions.GameFullException;
import exceptions.UsernameAlreadyUsedException;
import org.junit.Assert;
import org.junit.Test;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.UUID;

public class ServerInterfaceImplTest {

    private Registry registry;
    private DbInterface dbInterface;
    private int DBPORTNUMBER = 1300;

    @Test(expected = UsernameAlreadyUsedException.class)
    public void registerTest_expectToken() throws RemoteException {
        new Main().startServer();
        ServerInterface serverInterface = connectToApplicationServer(1200);
        String token = serverInterface.register("PindaKaas", "aPassword");
        System.out.println(token);
    }

    @Test
    public void loginTest_expectToken() throws RemoteException {
        new Main().startServer();
        ServerInterface serverInterface = connectToApplicationServer(1200);
        String token = serverInterface.login("PindaKaas", "aPassword");
        Assert.assertTrue(token != null && !token.isEmpty());
    }

    @Test
    public void addGame_expectID() throws RemoteException {
        new Main().startServer();
        ServerInterface serverInterface = connectToApplicationServer(1200);
        String username = UUID.randomUUID().toString();
        String token = serverInterface.register(username, "aPassword");
        String game_id = serverInterface.startNewGame(createNewGame(), token);
        Assert.assertTrue(game_id != null && !game_id.isEmpty());
    }

    @Test
    public void joinGame_expectNoError() throws RemoteException, GameFullException {
        new Main().startServer();
        ServerInterface serverInterface = connectToApplicationServer(1200);
        String username = UUID.randomUUID().toString();
        String token = serverInterface.register(username, "aPassword");
        String game_id = serverInterface.startNewGame(createNewGame(), token);
        System.out.println(token);

        serverInterface.joinGame(null, game_id, token);
        serverInterface.joinGame(null, game_id, token);
    }

    @Test(expected = GameFullException.class)
    public void joinGame_expectGameFullError() throws RemoteException, GameFullException {
        new Main().startServer();
        ServerInterface serverInterface = connectToApplicationServer(1200);
        String token = serverInterface.login("PindaKaas", "aPassword");
        System.out.println(token);
        String game_id = serverInterface.startNewGame(createNewGame(), token);
        System.out.println(game_id);

        serverInterface.joinGameAddBot(game_id, token);
        serverInterface.joinGameAddBot(game_id, token);
        boolean bool = serverInterface.joinGame(null, game_id, token);
    }

    @Test
    public void createGame_playWithBots() throws RemoteException {
        new Main().startServer();
        ServerInterface serverInterface = connectToApplicationServer(1200);
        String token = serverInterface.login("PindaKaas", "aPassword");
        String game_id = serverInterface.startNewGame(createNewGame(), token);
        serverInterface.joinGameAddBot(game_id, token);
        serverInterface.joinGameAddBot(game_id, token);
    }

    @Test
    public void createGame_playWithBots_checkIfInactive() throws RemoteException {
        new Main().startServer();
        ServerInterface serverInterface = connectToApplicationServer(1200);
        String token = serverInterface.login("PindaKaas", "aPassword");
        String game_id = serverInterface.startNewGame(createNewGame(), token);
        serverInterface.joinGameAddBot(game_id, token);
        serverInterface.joinGameAddBot(game_id, token);
        Assert.assertFalse(serverInterface.getGames(token).stream()
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

    private ServerInterface connectToApplicationServer(int portnumber) {
        try {
            return (ServerInterface) LocateRegistry.getRegistry("localhost", portnumber).lookup("UNOserver");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
