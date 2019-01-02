package client.views;

import applicationServer.ServerInterface;
import client.GameInfo;
import client.UserController;
import client.UserInfo;
import client.controller.GameController;
import client.controller.PopupNewGameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.util.UUID;

public class TestGameView extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/fxmlFiles/Game.fxml"));
        ServerInterface serverInterface = connectToApplicationServer(1200);
        UserController userController = new UserController(serverInterface);
        GameInfo newGame = createNewGame();

        userController.loginToServer("PindaKaas", "aPassword");

        String game_id = serverInterface.startNewGame(newGame);
        newGame.setGameID(game_id);
        userController.setGameInfo(newGame);
        serverInterface.joinGameAddBot(game_id);
        fxmlLoader.setController(new GameController(userController));
        Parent root1 = fxmlLoader.load();

        primaryStage.setTitle("Create new GameObject");
        primaryStage.setScene(new Scene(root1));
        primaryStage.show();
    }

    private ServerInterface connectToApplicationServer(int portnumber) {
        try {
            return (ServerInterface) LocateRegistry.getRegistry("localhost", portnumber).lookup("UNOserver");
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
