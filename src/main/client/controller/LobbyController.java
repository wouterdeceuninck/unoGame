package client.controller;


import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import exceptions.GameFullException;
import javafx.scene.control.*;
import client.GameInfo;
import client.UserController;
import exceptions.InvalidInputException;
import interfaces.lobbyInterface;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import applicationServer.uno.player.Player;

public class LobbyController extends UnicastRemoteObject implements lobbyInterface{

    public static final String GAME = "GameObject";
    private ObservableList gameData = FXCollections.observableArrayList();
    private ListView gamesList;
    private UserController userController;

    @FXML
    private Button btn_join, btn_new, btn_spectate, btn_exit, btn_send, btn_reload;

    @FXML
    Label lbl_username;

    @FXML
    private ListView<Player> players;

    @FXML
    private TextField chat_input;

    @FXML
    private TextArea chat;

    @FXML
    private AnchorPane pn_input, pn_output;
    private String username;

    public LobbyController(UserController userController) throws RemoteException {
        this.userController = userController;
    }

    public void initialize() throws RemoteException {

        gamesList = new ListView();
        gamesList.getSelectionModel().selectedItemProperty().addListener(
                (ChangeListener<String>) (observable, oldValue, newValue) -> {
                    List<String> temp = Arrays.asList(newValue.split("\t"));
                    setCurrentGame(this.userController.getGameInfo());
                });
        setList();
        chat.setEditable(false);
        lbl_username.setText(username);
        this.gamesList = new ListView(gameData);
    }

    private void setCurrentGame(GameInfo gameInfo) {
        this.userController.setGameInfo(gameInfo);
    }

    public void setList() throws RemoteException {
        reload();
        pn_input.getChildren().add(gamesList);
        gamesList.prefWidthProperty().bind(pn_input.widthProperty());
        gamesList.prefHeightProperty().bind(pn_input.heightProperty());
    }

    @FXML
    public void createNewGame() throws RemoteException {
    	startPopupNewGame();
    	reload();
    }

    public void createNewGameLogic() throws RemoteException, InvalidInputException {
        if (userController.getGameInfo().isValid()){
            userController.getServer().startNewGame(userController.getGameInfo());
        } else throw new InvalidInputException("GameObject info is not correct!");
    }

    @FXML
    public void joinGame() throws RemoteException {
		startGame();
		exit();
	}

    @FXML
    public void exit() throws RemoteException {
        Stage stage = (Stage) btn_exit.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    public void reload() throws RemoteException {
        List<String> gameslist = userController.getServer().getGames().stream()
                .map(gameInfo -> gameInfo.toString())
                .collect(Collectors.toList());
        gameData.clear();
        gameData.addAll(gameslist);
        gamesList.setEditable(false);
        gamesList.setItems(gameData);
    }

    public void startGame() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/client/fxmlFiles/Game.fxml"));
            GameController controller = new GameController(userController);
            fxmlLoader.setController(controller);
 			Parent root1 = (Parent) fxmlLoader.load();

            createStage(controller, root1, GAME).show();
            try {
                userController.getServer().joinGame(controller, userController.getGameInfo().getGameID()+ "", username);
            } catch (GameFullException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Stage createStage(GameController controller, Parent parent, String name) {
        Stage stage = new Stage();
        stage.setTitle(name);
        stage.setScene(new Scene(parent));
        setOnClose(controller, stage);
        return stage;
    }

    //TODO
    private void setOnClose(GameController controller, Stage stage) {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    userController.getServer().openLobby(controller);
                    LobbyController lobby = new LobbyController(userController);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/client/fxmlFiles/Lobby.fxml"));
                    fxmlLoader.setController(lobby);

                    Parent root1 = (Parent) fxmlLoader.load();

                    Stage stage = new Stage();
                    stage.setTitle("Lobby");
                    stage.setScene(new Scene(root1));
                    stage.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
	public void setMsg(String msg) {
		String message = chat.getText() + msg + "\n";
        chat.setText(message);
	}
	
	public void startPopupNewGame(){
		try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/client/fxmlFiles/PopupNewGame.fxml"));
            fxmlLoader.setController(new PopupNewGameController(this.userController));

            Parent root1 = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Create new GameObject");
            stage.setScene(new Scene(root1));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
	}

    public UserController getUserController() {
        return userController;
    }
}

