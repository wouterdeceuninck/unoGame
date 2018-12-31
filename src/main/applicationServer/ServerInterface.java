package applicationServer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import applicationServer.uno.cards.Card;
import client.GameInfo;
import exceptions.UnAutherizedException;
import exceptions.UsernameAlreadyUsedException;
import interfaces.gameControllerInterface;

public interface ServerInterface extends Remote {

    String startNewGame(GameInfo gameInfo) throws RemoteException;
	boolean joinGame(gameControllerInterface gameController, String gameID, String username) throws RemoteException;
	boolean joinGameAddBot(String gameID) throws RemoteException;

	List<GameInfo> getGames() throws RemoteException;
	void sendGameMsg(String msg, String gameID, String username) throws RemoteException;
	List<Card> getCards(String username, String gameID) throws RemoteException;
	void openLobby(gameControllerInterface gci) throws RemoteException;

    String register(String username, String password) throws RemoteException, UsernameAlreadyUsedException;
    String login(String username, String password) throws RemoteException, UnAutherizedException;

    int getAmountOfGameOnServer() throws RemoteException;
}
