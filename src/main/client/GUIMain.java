package client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Logger;


public class GUIMain extends Application {

	public static String TOKEN;
	public static Logger LOGGER = Logger.getLogger("logger");
	@Override
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/client/fxmlFiles/Login.fxml"));
		stage.setTitle("UNO Login");
		stage.setScene(new Scene(root, 800, 450));
		stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}

	public static void setTOKEN(String token) {
		TOKEN = token;
	}
}
