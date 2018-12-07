package main.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import main.controller.NewGameInfo;
import main.exceptions.UsernameAlreadyUsedException;
import main.uno.Card;

public interface ServerInterface extends Remote, AuthenticationInterface {

    void startNewGame(NewGameInfo newGameInfo) throws RemoteException;
	void giveLobby(lobbyInterface lobbyController) throws RemoteException;
	void exit(lobbyInterface lobbyController) throws RemoteException;
	void joinGame(gameControllerInterface gameController, int gameID, String username) throws RemoteException;

	List<String> getGames() throws RemoteException;
	void send(String s, String username) throws RemoteException;
	void sendGameMsg(String msg, int gameID, String username) throws RemoteException;
	List<Card> getCards(String username, String gameID) throws RemoteException;
	void openLobby(gameControllerInterface gci) throws RemoteException;

    String register(String username, String password) throws RemoteException, UsernameAlreadyUsedException;
    String login(String username, String password) throws RemoteException;
}
