package databaseServer;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.List;

import client.GameInfo;
import exceptions.GameFullException;
import exceptions.UnAutherizedException;
import exceptions.UsernameAlreadyUsedException;

public interface DbInterface extends Remote {
	void interconnectDBServers()throws RemoteException;

	String loginUser(String username, String password)throws UnAutherizedException, RemoteException;
	String registerUser(String username, String password) throws UsernameAlreadyUsedException, RemoteException;

	List<GameInfo> getActiveGames() throws RemoteException;

	void setInactive(String game_id)throws RemoteException;
	void duplicateAddUser(String username, String password, String token, Timestamp timestamp)throws RemoteException;

	String addGame(GameInfo gameInfo, int serverport)throws RemoteException;
	void duplicateAddGame(GameInfo gameInfo, int serverport)throws RemoteException;

	void addUsersToGame(String game_id) throws RemoteException;
	void removeUsersFromGame(String game_id)throws RemoteException;

	void duplicateAddUsersToGame(String game_id)throws RemoteException;
	void duplicateremoveUsersFromGame(String game_id)throws RemoteException;

}
