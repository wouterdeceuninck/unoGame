package main.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import main.exceptions.UnAutherizedException;
import main.exceptions.UsernameAlreadyUsedException;
import main.applicationServer.uno.Card;

public interface dbInterface extends Remote {
	/**
	 * @throws RemoteException
	 */
	public void setDatabaseServers() throws RemoteException;

	/**
	 * @param portnumber
	 * @throws RemoteException
	 */
	public void ping(int portnumber) throws RemoteException;

	/**
	 * @return
	 * @throws RemoteException
	 */
	public int getPortnumber() throws RemoteException;

	/**
	 * @param username
	 * @return
	 * @throws RemoteException
	 */
	public boolean checkUsername(String username) throws RemoteException;

	/**
	 * @param username
	 * @param password
	 * @return
	 * @throws RemoteException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 * @throws SQLException 
	 */
	public boolean loginUser(String username, String password)throws RemoteException, UnAutherizedException;

	/**
	 * @return
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public List<String> getActiveGames() throws RemoteException, SQLException;

	/**
	 * @param game_id
	 * @throws RemoteException
	 */
	public void StopGame(int game_id) throws RemoteException;

	/**
	 * @param username
	 * @param password
	 * @throws RemoteException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 */
	public void addUser(String username, String password)
			throws RemoteException, UsernameAlreadyUsedException;

	/**
	 * @param username
	 * @param password
	 * @param token
	 * @param timestamp
	 * @throws RemoteException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 */
	public void duplicateAddUser(String username, String password, String token, Timestamp timestamp)
			throws RemoteException, InvalidKeyException, SignatureException;

	/**
	 * @param id
	 * @param name
	 * @param aantalSpelers
	 * @param serverport
	 * @param theme
	 * @return
	 * @throws RemoteException
	 */
	String addGame(int id, String name, int aantalSpelers, int serverport, int theme) throws RemoteException;

	/**
	 * @param id
	 * @param name
	 * @param aantalSpelers
	 * @param serverport
	 * @param theme
	 * @throws RemoteException
	 */
	void duplicateAddGame(String id, String name, int aantalSpelers, int serverport, int theme) throws RemoteException;

	/**
	 * @param game_name
	 * @param users
	 * @throws RemoteException
	 */
	void addUsersToGame(String game_name, List<String> users) throws RemoteException;

	/**
	 * @param game_id
	 * @param users
	 * @throws RemoteException
	 */
	void duplicateAddUsersToGame(int game_id, List<String> users) throws RemoteException;

	/**
	 * @param id
	 * @throws RemoteException
	 */
	public void createPlayerHandTabel(String id) throws RemoteException;

	/**
	 * @param id
	 * @throws RemoteException
	 */
	public void duplicateCreatePlayerHand(String id) throws RemoteException;

	/**
	 * @param name
	 * @param cards
	 * @param id
	 * @throws RemoteException
	 */
	public void updateHandPlayer(String name, List<Card> cards, String id) throws RemoteException;

	/**
	 * @param name
	 * @param cards
	 * @param id
	 * @throws RemoteException
	 */
	public void duplicateUpdateHandPlayer(String name, List<Card> cards, String id) throws RemoteException;

	/**
	 * @param user_id
	 * @return
	 * @throws RemoteException
	 * @throws SQLException
	 */
	public String getPlayerHand(int user_id) throws RemoteException, SQLException;

}
