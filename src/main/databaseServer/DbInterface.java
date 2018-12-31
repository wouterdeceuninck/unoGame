package databaseServer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.List;

import client.GameInfo;
import databaseServer.businessObjects.GameObject;
import exceptions.GameFullException;
import exceptions.UnAutherizedException;
import exceptions.UsernameAlreadyUsedException;

public interface DbInterface extends Remote {
	void connectToDBServer(DbInterface dbInterface)throws RemoteException;

	String loginUser(String username, String password)throws UnAutherizedException, RemoteException;
	void duplicateLoginUser(String username, String token)throws RemoteException;

	String registerUser(String username, String password) throws UsernameAlreadyUsedException, RemoteException;
	void duplicateRegisterUser(String username, String password, String token)throws RemoteException;

	List<GameInfo> getActiveGames() throws RemoteException;

	void setInactive(String game_id)throws RemoteException;
	void duplicateSetInactive(String game_id) throws RemoteException;

	String addGame(GameInfo gameInfo, int serverport)throws RemoteException;
	void duplicateAddGame(GameObject gameObject, String game_id)throws RemoteException;

	boolean addUsersToGame(String game_id) throws RemoteException;
	void duplicateAddUsersToGame(String game_id)throws RemoteException;

	void removeUsersFromGame(String game_id)throws RemoteException;
	void duplicateRemoveUsersFromGame(String game_id)throws RemoteException;

	String getUser_forTestsOnly(String username) throws RemoteException;
}
