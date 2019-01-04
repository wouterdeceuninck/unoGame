package client.views;

import applicationServer.ServerInterface;
import client.businessObjects.GameInfo;
import client.businessObjects.UserInfo;
import client.controller.GameController;
import dispatcher.Main;
import exceptions.GameFullException;
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
    private final String username = "PindaKaas";

    @Override
    public void start(Stage primaryStage) throws Exception {
        new Main().startServer();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/fxmlFiles/Game.fxml"));

        ServerInterface serverInterface = connectToApplicationServer(1200);
        UserInfo userInfo = createNewUser(username);
        GameInfo gamInfo = createNewGame();

        String token = serverInterface.login("PindaKaas", "aPassword");
        userInfo.setToken(token);

        String game_id = serverInterface.startNewGame(gamInfo, token);
        gamInfo.setGameID(game_id);
        try {
            serverInterface.joinGameAddBot(game_id, token);
        } catch (GameFullException e) {
            e.printStackTrace();
        }
        GameController controller = new GameController(userInfo, gamInfo, serverInterface);
        fxmlLoader.setController(controller);
        Parent root1 = fxmlLoader.load();

        primaryStage.setTitle("Create new GameObject");
        primaryStage.setScene(new Scene(root1));
        try {
            serverInterface.joinGame(controller, game_id, token);
        } catch (GameFullException e) {
            e.printStackTrace();
        }
        primaryStage.show();
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

    private GameInfo createNewGame() {
        return new GameInfo.Builder()
                .setGameID(UUID.randomUUID().toString())
                .setGameName("myNewGame")
                .setAmountOfPlayers(2)
                .setGameTheme(0)
                .build();
    }
}
