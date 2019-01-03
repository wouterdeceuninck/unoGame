package applicationServer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import client.businessObjects.GameInfo;
import exceptions.UnAutherizedException;
import exceptions.UsernameAlreadyUsedException;
import interfaces.gameControllerInterface;

public interface ServerInterface extends Remote {

    String startNewGame(GameInfo gameInfo, String token) throws RemoteException;
	boolean joinGame(gameControllerInterface gameController, String gameID, String token) throws RemoteException;
	boolean joinGameAddBot(String gameID, String token) throws RemoteException;
	boolean leaveGame(String gameID, String token)throws RemoteException;

	List<GameInfo> getGames(String token) throws RemoteException;
	void sendGameMsg(String msg, String gameID, String token) throws RemoteException;
	void openLobby(gameControllerInterface gci) throws RemoteException;

    String register(String username, String password) throws RemoteException, UsernameAlreadyUsedException;
    String login(String username, String password) throws RemoteException, UnAutherizedException;

    int getAmountOfGameOnServer() throws RemoteException;
}
