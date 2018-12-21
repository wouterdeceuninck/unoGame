package main.client.controller;

import main.client.GameInfo;
import main.client.UserController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.rmi.RemoteException;

public class PopupNewGameController {
	private final UserController userController;

	ObservableList<Integer> themeList = FXCollections.observableArrayList(0, 1);
	ObservableList<Integer> numberOfPlayerList = FXCollections.observableArrayList(1, 2, 3, 4);

	public PopupNewGameController(UserController userController) {
		this.userController = userController;
	}

	@FXML
	TextField name;

	@FXML
	ChoiceBox<Integer> themePicker = new ChoiceBox<>(themeList);

	@FXML
	ChoiceBox<Integer> numberOfPlayersnew = new ChoiceBox<>(numberOfPlayerList);

	@FXML
	Button btn_start, btn_cancel;

	@FXML
	public void initialize() {
		themePicker.setItems(themeList);
		numberOfPlayersnew.setItems(numberOfPlayerList);
	}

	@FXML
	public void startGame(final String gameId, final int gameTheme, final String gameName, final int amountOfPlayers) throws RemoteException {
		userController.setGameInfo(new GameInfo(gameId, gameTheme, gameName, amountOfPlayers));
	    userController.getServer().startNewGame(userController.getGameInfo());
		closeWindow();
	}

    private String getGameName(String gameName) {
		if (gameName.equals("")) {
			gameName = userController.getUserInfo().getUsername() + "'s game";
		}
		return gameName;
	}

	@FXML
	public void cancel() {
		closeWindow();
	}

	public void closeWindow() {
		Stage stage = (Stage) btn_cancel.getScene().getWindow();
		stage.close();
	}
}
