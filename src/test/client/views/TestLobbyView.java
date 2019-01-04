package client.views;

import applicationServer.ServerInterface;
import client.businessObjects.UserInfo;
import client.controller.LobbyController;
import dispatcher.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

public class TestLobbyView extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        new Main().startServer();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/fxmlFiles/Lobby.fxml"));
        ServerInterface serverInterface = connectToApplicationServer(1200);
        UserInfo newUser = getNewUser();
        newUser.setToken(serverInterface.login("PindaKaas", "aPassword"));
        fxmlLoader.setController(new LobbyController(newUser, serverInterface));
        Parent root1 = fxmlLoader.load();

        primaryStage.setTitle("GameObject Lobby");
        primaryStage.setScene(new Scene(root1));
        primaryStage.show();
    }

    private UserInfo getNewUser() {
        return new UserInfo.InnerBuilder().setName("PindaKaas").buildUserInfo();
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
