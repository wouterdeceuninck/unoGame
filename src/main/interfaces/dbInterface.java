package main.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import main.client.GameInfo;
import main.exceptions.UnAutherizedException;
import main.exceptions.UsernameAlreadyUsedException;
import main.applicationServer.uno.Card;

public interface dbInterface extends Remote {
	void setDatabaseServers();

	boolean checkUsername(String username) throws RemoteException;
	boolean loginUser(String username, String password)throws RemoteException, UnAutherizedException;

	List<GameInfo> getActiveGames() throws RemoteException;
	void setInactive(String game_id) throws RemoteException;

	void addUser(String username, String password) throws RemoteException, UsernameAlreadyUsedException;
	void duplicateAddUser(String username, String password, String token, Timestamp timestamp) throws RemoteException;

	String addGame(String id, String name, int aantalSpelers, int serverport, int theme) throws RemoteException;
	void duplicateAddGame(String id, String name, int aantalSpelers, int serverport, int theme) throws RemoteException;

	void addUsersToGame(String game_name, List<String> users) throws RemoteException;
	void duplicateAddUsersToGame(String game_id, List<String> users) throws RemoteException;

}
