package client.views;

import applicationServer.ServerInterface;
import client.UserController;
import client.controller.PopupNewGameController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class TestPopupView extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/fxmlFiles/PopupNewGame.fxml"));
        ServerInterface serverInterface = connectToApplicationServer(1200);
        fxmlLoader.setController(new PopupNewGameController(new UserController(serverInterface)));
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

}
