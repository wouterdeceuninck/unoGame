package client.views;

import applicationServer.ServerInterface;
import client.UserController;
import client.controller.LobbyController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class TestLobbyView extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/fxmlFiles/Lobby.fxml"));
        ServerInterface serverInterface = connectToApplicationServer(1200);
        fxmlLoader.setController(new LobbyController(new UserController(serverInterface)));
        Parent root1 = fxmlLoader.load();

        primaryStage.setTitle("GameObject Lobby");
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
