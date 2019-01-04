package intergration;

import applicationServer.ServerInterface;
import client.businessObjects.GameInfo;
import databaseServer.DbInterface;
import dispatcher.Main;
import exceptions.GameFullException;
import org.junit.Assert;
import org.junit.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.List;
import java.util.UUID;

public class LobbyControllerTest {

    @Test
    public void getActiveGames() throws RemoteException, GameFullException {
        new Main().startServer();
        ServerInterface serverInterface = connectToApplicationServer(1200);
        String token = serverInterface.register(UUID.randomUUID().toString(), "aPassword");

        serverInterface.getGames(token);
        String game_id = serverInterface.startNewGame(createNewGame(), token);

        serverInterface.joinGame(null, game_id, token);
        List<GameInfo> games = serverInterface.getGames(token);
        Assert.assertTrue(games.size() == 1);

        games = serverInterface.getGames(token);
        Assert.assertTrue(games.size() == 1);

        game_id = serverInterface.startNewGame(createNewGame(), token);
        serverInterface.joinGame(null, game_id, token);
        games = serverInterface.getGames(token);
        Assert.assertTrue(games.size() == 2);

        games = serverInterface.getGames(token);
        Assert.assertTrue(games.size() == 2);
    }

    private ServerInterface connectToApplicationServer(int portnumber) {
        try {
            return (ServerInterface) LocateRegistry.getRegistry("localhost", portnumber).lookup("UNOserver");
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
