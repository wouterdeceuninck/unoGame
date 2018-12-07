package main.controller;


import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;

import main.client.GameData;
import main.client.GameInfo;
import main.client.UserController;
import main.interfaces.lobbyInterface;
import main.interfaces.ServerInterface;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.uno.Player;

public class LobbyController extends UnicastRemoteObject implements lobbyInterface{

    public static final String GAME = "Game";
    ServerInterface server;
    private int gameID;
    private String gameName;
    private int gameTheme;
    private ObservableList gameData = FXCollections.observableArrayList();
    private ListView gamesList = new ListView(gameData);
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

    public LobbyController(UserController userController, ServerInterface server) throws RemoteException {
        this.userController = userController;
        this.server = server;
        username = userController.getUserInfo().getUsername();
    }

    public void initialize() throws RemoteException {
        setList();
        server.giveLobby(this);

        gamesList.getSelectionModel().selectedItemProperty().addListener(
                (ChangeListener<String>) (observable, oldValue, newValue) -> {
                    setCurrentGame(Arrays.asList(newValue.split("\t")));
                });
        chat.setEditable(false);
        lbl_username.setText(username);
    }

    private void setCurrentGame(List<String> temp) {
        gameID = Integer.parseInt(temp.get(0).substring(0, temp.get(0).length()-4));
        gameName = temp.get(1);
        gameTheme = Integer.parseInt(temp.get(3));
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

    @FXML
    public void joinGame() throws RemoteException {
		startGame();
		exit();
	}

    @FXML
    public void exit() throws RemoteException {
    	server.exit(this);
        Stage stage = (Stage) btn_exit.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void send() throws RemoteException {
    	server.send(chat_input.getText(), this.username);
        chat_input.setText("");
    }
    
    @FXML
    public void reload() throws RemoteException {
        List<String> gameslist = server.getGames();
        gameData.clear();
        gameData.addAll(gameslist);
        gamesList.setEditable(false);
        gamesList.setItems(gameData);
    }

    public void startGame() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/fxmlFiles/Game.fxml"));
            userController.setGameInfo(new GameInfo(gameID, gameTheme, gameName));
            GameController controller = new GameController(userController, server);
            fxmlLoader.setController(controller);
 			Parent root1 = (Parent) fxmlLoader.load();

            createStage(controller, root1, GAME).show();
            server.joinGame(controller, gameID, username);
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
                    server.openLobby(controller);
                    LobbyController lobby = new LobbyController(userController, server);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/fxmlFiles/Lobby.fxml"));
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/fxmlFiles/PopupNewGame.fxml"));
            PopupNewGameController controller = new PopupNewGameController(this.username, server);
            fxmlLoader.setController(controller);

            
            Parent root1 = (Parent) fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Create new Game");
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
