package client.controller;


import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import applicationServer.ServerInterface;
import client.businessObjects.UserInfo;
import exceptions.GameFullException;
import exceptions.RerouteNeededExeption;
import javafx.scene.control.*;
import client.businessObjects.GameInfo;
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
    private ServerInterface serverInterface;
    private UserInfo userInfo;
    private GameInfo gameInfo;

    public LobbyController(UserInfo userInfo, ServerInterface serverInterface) throws RemoteException {
        this.userInfo = userInfo;
        this.serverInterface = serverInterface;
    }

    public void initialize() throws RemoteException {
        gamesList = new ListView();
        gamesList.getSelectionModel().selectedItemProperty().addListener(
                (ChangeListener<String>) (observable, oldValue, newValue) -> {
                    List<String> temp = Arrays.asList(newValue.split("\t"));
                    setCurrentGame(buildGameInfo(temp));
                });
        setList();
        lbl_username.setText(userInfo.getUsername());
        this.gamesList = new ListView(gameData);
    }

    private GameInfo buildGameInfo(List<String> temp) {
        String[] amountOfPlayers = temp.get(2).split("/");
        return new GameInfo.Builder()
                .setGameID(temp.get(0))
                .setGameName(temp.get(1))
                .setConnectedPlayers(Integer.parseInt(amountOfPlayers[0]))
                .setAmountOfPlayers(Integer.parseInt(amountOfPlayers[1]))
                .setGameTheme(Integer.parseInt(temp.get(3)))
                .build();
    }

    private void setCurrentGame(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
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
        if (gameInfo.isValid()){
            serverInterface.startNewGame(gameInfo, userInfo.getToken());
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
        gameData.clear();
        gameData.addAll(
                serverInterface.getGames(userInfo.getToken()).stream()
                    .map(GameInfo::toString)
                    .collect(Collectors.toList())
        );
        gamesList.setEditable(false);
        gamesList.setItems(gameData);
    }

    public void startGame() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/fxmlFiles/Game.fxml"));
            GameController controller = new GameController(userInfo, gameInfo , serverInterface);
            fxmlLoader.setController(controller);
 			Parent root1 = (Parent) fxmlLoader.load();

            createStage(controller, root1, "GameObject").show();
            try {
                serverInterface.joinGame(controller, gameInfo.getGameID()+ "", userInfo.getToken());
            } catch (GameFullException e) {
                e.printStackTrace();
            } catch (RerouteNeededExeption rerouteNeededExeption) {
                this.serverInterface = connectToApplicationServer(Integer.parseInt(rerouteNeededExeption.getMessage()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private ServerInterface connectToApplicationServer(int leastLoadedApplicationServer) throws RemoteException, NotBoundException {
        System.out.println(leastLoadedApplicationServer);
        return (ServerInterface) LocateRegistry.getRegistry("localhost", leastLoadedApplicationServer).lookup("UNOserver");
    }

    private Stage createStage(GameController controller, Parent parent, String name) {
        Stage stage = new Stage();
        stage.setTitle(name);
        stage.setScene(new Scene(parent));
        setOnClose(controller, stage);
        return stage;
    }

    private void setOnClose(GameController controller, Stage stage) {
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                try {
                    serverInterface.openLobby(controller);
                    LobbyController lobby = new LobbyController(userInfo, serverInterface);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/fxmlFiles/Lobby.fxml"));
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/client/fxmlFiles/PopupNewGame.fxml"));
            fxmlLoader.setController(new PopupNewGameController(this.userInfo, this.serverInterface));

            Parent root1 = fxmlLoader.load();

            Stage stage = new Stage();
            stage.setTitle("Create new GameObject");
            stage.setScene(new Scene(root1));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
	}

    public UserInfo getUserInfo() {
        return userInfo;
    }

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

    private ObservableList gameData = FXCollections.observableArrayList();
    private ListView gamesList;

}

