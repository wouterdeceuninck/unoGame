package client.views;

import dispatcher.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TestLoginView extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        new Main().startServer();
        Parent root = FXMLLoader.load(getClass().getResource("/client/fxmlFiles/Login.fxml"));
        primaryStage.setTitle("UNO Login");
        primaryStage.setScene(new Scene(root, 800, 450));
        primaryStage.show();
    }
}
