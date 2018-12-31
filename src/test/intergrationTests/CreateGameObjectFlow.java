package intergrationTests;

import client.GameInfo;
import client.UserController;
import client.controller.LobbyController;
import client.controller.LoginController;
import dispatcher.Main;
import exceptions.InvalidInputException;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.UUID;

public class CreateGameObjectFlow {

    private void startDispatcher() {
        new Main().startServer(10);
    }

    private LobbyController getLobbyController() throws InvalidInputException, RemoteException {
        startDispatcher();
        LoginController loginController = new LoginController();
        loginController.loginLogic("aValidUsername", "aValidUsername");
        return new LobbyController(loginController.getUserController());
    }

    @Test
    public void createGame() throws InvalidInputException, RemoteException {
        LobbyController lobbyController = getLobbyController();
        lobbyController.getUserController().setGameInfo(createNewGameInfo("MyNewGame", 3, 1));
        lobbyController.createNewGameLogic();
    }

    private GameInfo createNewGameInfo(String myNewGame, int amountOfPlayers, int gameTheme) {
        return new GameInfo.Builder()
                .setGameID(UUID.randomUUID().toString())
                .setGameName(myNewGame)
                .setAmountOfPlayers(amountOfPlayers)
                .setGameTheme(gameTheme).build();
    }

    @Test(expected = InvalidInputException.class)
    public void createGame_invalidGameName() throws InvalidInputException, RemoteException {
        LobbyController lobbyController = new LobbyController(new UserController(null));
        lobbyController.getUserController().setGameInfo(createNewGameInfo("", 3, 1));
        lobbyController.createNewGameLogic();
    }

    @Test(expected = InvalidInputException.class)
    public void createGame_invalidAmountOfPlayers() throws InvalidInputException, RemoteException {
        LobbyController lobbyController = new LobbyController(new UserController(null));
        lobbyController.getUserController().setGameInfo(createNewGameInfo("myNewGame", 5, 1));
        lobbyController.createNewGameLogic();
    }
}
