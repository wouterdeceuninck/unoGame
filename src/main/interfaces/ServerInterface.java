package main.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import main.client.GameInfo;
import main.exceptions.UsernameAlreadyUsedException;
import main.applicationServer.uno.Card;

public interface ServerInterface extends Remote, AuthenticationInterface {

    String startNewGame(GameInfo gameInfo) throws RemoteException;
	void joinGame(gameControllerInterface gameController, String gameID, String username) throws RemoteException;

	List<String> getGames() throws RemoteException;
	void sendToAllPlayers(String s, String username) throws RemoteException;
	void sendGameMsg(String msg, String gameID, String username) throws RemoteException;
	List<Card> getCards(String username, String gameID) throws RemoteException;
	void openLobby(gameControllerInterface gci) throws RemoteException;

    String register(String username, String password) throws RemoteException, UsernameAlreadyUsedException;
    String login(String username, String password) throws RemoteException;
}
