package main.client.controller;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import main.client.UserController;
import main.exceptions.InvalidInputException;
import main.interfaces.dispatcherInterface;
import main.interfaces.ServerInterface;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.security.auth.login.FailedLoginException;

public class LoginController {

	public ServerInterface server = null;
	private String username = null;
	private UserController userController = null;
    private static final int DISPATCHER_PORT = 1099;

	@FXML
	private Button btn_Login;

	@FXML
	private Pane pn_Login, pn_Register;

	@FXML
	public TextField registerUsername, loginUsername;

	@FXML
	public PasswordField password1, password2, loginPassword;

	//TODO
	@FXML
	private DatePicker birthdate;

	@FXML
	private void SignUpUp() {
		pn_Login.setVisible(false);
		pn_Register.setVisible(true);
	}

	@FXML
	private void SignInUp() {
		pn_Login.setVisible(true);
		pn_Register.setVisible(false);
	}

	public LoginController() {
		try {
			connectToServer();
		} catch (RemoteException | NotBoundException e) {
			System.out.println("Server is not found!");
		}
	}

	public void initialize(){
		try {
			connectToServer();
		} catch (RemoteException | NotBoundException e) {
			popUpAlert("Could not connect to the server");
		}

	}

	private void connectToServer() throws RemoteException, NotBoundException {
		server = (ServerInterface) LocateRegistry.getRegistry("localhost", (
				(dispatcherInterface) LocateRegistry.getRegistry("localhost", DISPATCHER_PORT)
					.lookup("UNOdispatcher")).getPort())
				.lookup("UNOserver");
		userController = new UserController(server);
	}

	@FXML
    private void Register() {
			try {
				registerLogic(registerUsername.getText(), password1.getText(), password2.getText());
				startLobby();
			} catch (FailedLoginException | InvalidInputException e) {
				popUpAlert(e.getMessage());
			}
	}

	@FXML
	private void login() {
		try {
			this.loginLogic(loginUsername.getText(), loginPassword.getText());
			startLobby();
		} catch (InvalidInputException e) {
			popUpAlert(e.getMessage());
		}
	}

	public void registerLogic(final String username, final String password1, final String password2) throws FailedLoginException, InvalidInputException {
		if (isValidRegisterInput(username, password1, password2)) {
			try {
				userController.registerToServer(username, password1);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		} else throw new InvalidInputException("The input is invalid");

	}


	public void loginLogic(String username, String password) throws InvalidInputException {
		if (isValidLoginInput(username, password)) {
            try {
                userController.loginToServer(username, password);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else throw new InvalidInputException("The input is invalid");
	}

	public boolean isValidLoginInput(String username, String password) {
		return username.length() >= 6 && password.length() >= 6;
	}

    public boolean isValidRegisterInput(String username, String password1, String password2) {
        return username.length() >= 6 && password1.length() >= 6 && password2.length() >= 6 && password1.equals(password2);
    }

	private void closeWindow() {
		Stage stage = (Stage) btn_Login.getScene().getWindow();
		stage.close();
	}

	private void popUpAlert(String string) {
		Alert errorAlert = new Alert(AlertType.ERROR);
		errorAlert.setHeaderText("Input not valid");
		errorAlert.setContentText(string);
		errorAlert.showAndWait();
	}

	private void startLobby() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/main/client/fxmlFiles/Lobby.fxml"));
			fxmlLoader.setController(new LobbyController(userController));
			Parent root1 = fxmlLoader.load();

			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Game Lobby");
			stage.setScene(new Scene(root1));
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
		closeWindow();
	}

	private void serverNotFound() {
		popUpAlert("Connection to server lost");
	}

    public UserController getUserController() {
        return userController;
    }
}
