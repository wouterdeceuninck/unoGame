package test.intergrationTests;

import main.client.GameInfo;
import main.client.controller.LobbyController;
import main.client.controller.LoginController;
import main.dispatcher.Main;
import main.exceptions.InvalidInputException;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.UUID;

public class JoinGameFlow {
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
}
