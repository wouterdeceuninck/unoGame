package presentatie;

import applicationServer.ServerInterface;
import client.businessObjects.GameInfo;
import client.businessObjects.UserInfo;
import client.controller.GameController;
import databaseServer.security.JwtFactory;
import databaseServer.security.Token;
import dispatcher.DispatcherInterface;
import dispatcher.Main;
import exceptions.GameFullException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.Test;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.time.LocalDateTime;
import java.util.UUID;

public class AddBotToGame extends Application {

    private ServerInterface connectToApplicationServer(int portnumber) {
        try {
            return (ServerInterface) LocateRegistry.getRegistry("localhost", portnumber).lookup("UNOserver");
        } catch (RemoteException | NotBoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private DispatcherInterface connectToDispatcher() {
        try {
            return (DispatcherInterface) LocateRegistry.getRegistry("localhost", 1099).lookup("UNOdispatcher");
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

    @Override
    public void start(Stage primaryStage) throws Exception {
        new Main().startServer();
        DispatcherInterface dispatcherInterface = connectToDispatcher();
        ServerInterface serverInterface = connectToApplicationServer(dispatcherInterface.getLeastLoadedApplicationServer());
        String adminUserToken = getAdminUser();

        GameInfo gameInfo = createNewGame();
        String username = UUID.randomUUID().toString();
        UserInfo userInfo = new UserInfo.InnerBuilder().setName(username).buildUserInfo();
        userInfo.setToken(serverInterface.register(username, "aRandomPassword"));

        String game_id = serverInterface.startNewGame(gameInfo, adminUserToken);
        gameInfo.setGameID(game_id);

        GameController controller = new GameController(userInfo, gameInfo, serverInterface);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/fxmlFiles/Game.fxml"));
        fxmlLoader.setController(controller);

        Parent root1 = fxmlLoader.load();
        primaryStage.setTitle("Create new GameObject");
        primaryStage.setScene(new Scene(root1));
        primaryStage.show();

        try {
            serverInterface.joinGame(controller, game_id, userInfo.getToken());
            serverInterface.joinGameAddBot(game_id, adminUserToken);
        } catch (GameFullException e) {
            e.printStackTrace();
        }
    }

    private String getAdminUser() {
        return new Token.Builder()
                .setAlg("SHA256")
                .setName("AdminUser")
                .setTimestamp(LocalDateTime.now().toString())
                .build()
                .toString();
    }
}
