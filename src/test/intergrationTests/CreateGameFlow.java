package test.intergrationTests;

import main.client.GameInfo;
import main.client.UserController;
import main.controller.LobbyController;
import main.controller.LoginController;
import main.dispatcher.Main;
import main.exceptions.InvalidInputException;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.UUID;

public class CreateGameFlow {

    private void startDispatcher() {
        new Main().startServer();
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
        lobbyController.getUserController().setGameInfo(new GameInfo(UUID.randomUUID().toString(), 1, "MyNewGame", 3));
        lobbyController.createNewGameLogic();
    }

    @Test(expected = InvalidInputException.class)
    public void createGame_invalidGameName() throws InvalidInputException, RemoteException {
        LobbyController lobbyController = new LobbyController(new UserController(null));
        lobbyController.getUserController().setGameInfo(new GameInfo(UUID.randomUUID().toString(), 1, "", 3));
        lobbyController.createNewGameLogic();
    }

    @Test(expected = InvalidInputException.class)
    public void createGame_invalidGameID() throws InvalidInputException, RemoteException {
        LobbyController lobbyController = new LobbyController(new UserController(null));
        lobbyController.getUserController().setGameInfo(new GameInfo(UUID.randomUUID().toString(), 0, "aValidGameName", 3));
        lobbyController.createNewGameLogic();
    }

    @Test(expected = InvalidInputException.class)
    public void createGame_invalidAmountOfPlayers() throws InvalidInputException, RemoteException {
        LobbyController lobbyController = new LobbyController(new UserController(null));
        lobbyController.getUserController().setGameInfo(new GameInfo(UUID.randomUUID().toString(), 1, "", 0));
        lobbyController.createNewGameLogic();
    }
}
