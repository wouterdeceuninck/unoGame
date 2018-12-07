package main.controller;

import main.interfaces.ServerInterface;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.rmi.RemoteException;

public class PopupNewGameController {
	private String username;
	private ServerInterface server;
	ObservableList<Integer> themeList = FXCollections.observableArrayList(0, 1);
	ObservableList<Integer> numberOfPlayerList = FXCollections.observableArrayList(1, 2, 3, 4);

	public PopupNewGameController(String username, ServerInterface server) {
		this.username = username;
		this.server = server;
		themePicker.setItems(themeList);
		numberOfPlayersnew.setItems(numberOfPlayerList);
	}

	@FXML
	TextField name;

	@FXML
	ChoiceBox<Integer> themePicker = new ChoiceBox<>(themeList);

	@FXML
	ChoiceBox<Integer> numberOfPlayersnew = new ChoiceBox<>(numberOfPlayerList);

	@FXML
	TextField playerAmount;

	@FXML
	Button btn_start, btn_cancel;

	@FXML
	public void initialize() {
		themePicker.setItems(themeList);
		numberOfPlayersnew.setItems(numberOfPlayerList);
	}

	@FXML
	public void startGame() throws RemoteException {
	    server.startNewGame(new NewGameInfo(getGameName(name.getText()), themePicker.getValue(), numberOfPlayersnew.getValue()));
		closeWindow();
	}

    private String getGameName(String gameName) {
		if (gameName.equals("")) {
			gameName = username + "'s game";
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
