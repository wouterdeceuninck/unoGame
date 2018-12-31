package intergrationTests;

import client.GameInfo;
import client.controller.LobbyController;
import client.controller.LoginController;
import dispatcher.Main;
import exceptions.InvalidInputException;
import org.junit.Test;

import java.rmi.RemoteException;
import java.util.UUID;

public class JoinGameObjectFlow {
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
        lobbyController.getUserController().setGameInfo(new GameInfo.Builder()
                .setGameID(UUID.randomUUID().toString())
                .setGameName("myNewGame")
                .setAmountOfPlayers(3)
                .setGameTheme(1)
                .build());
        lobbyController.createNewGameLogic();
    }
}
