package client.controller;

import applicationServer.ServerInterface;
import client.businessObjects.GameInfo;
import client.businessObjects.UserInfo;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.rmi.RemoteException;

public class PopupNewGameController {

	private final ServerInterface serverInterface;
	private final UserInfo userInfo;

	public PopupNewGameController(UserInfo userInfo , ServerInterface serverInterface) {
		this.userInfo = userInfo;
		this.serverInterface = serverInterface;
	}

	private GameInfo createGameInfo() {
		return new GameInfo.Builder()
			.setGameName(name.getText())
			.setAmountOfPlayers(numberOfPlayersnew.getValue())
			.setGameTheme(themePicker.getValue())
			.build();
	}

	private String getGameName(String gameName) {
		if (gameName.equals("")) {
			gameName = userInfo.getUsername() + "'s game";
		}
		return gameName;
	}

	private GameInfo buildNewGameInfo() {
		return new GameInfo.Builder()
				.setGameName(getGameName(name.getText()))
				.setGameTheme(themePicker.getValue())
				.setAmountOfPlayers(numberOfPlayersnew.getValue())
				.build();
	}

	ObservableList<Integer> themeList = FXCollections.observableArrayList(0, 1);
	ObservableList<Integer> numberOfPlayerList = FXCollections.observableArrayList(1, 2, 3, 4);

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
	public void startGame() throws RemoteException {
		createGameInfo();
		serverInterface.startNewGame(buildNewGameInfo(), userInfo.getToken());
		closeWindow();
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
