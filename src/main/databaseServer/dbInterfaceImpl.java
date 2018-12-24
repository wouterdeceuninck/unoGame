package main.databaseServer;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import main.exceptions.UnAutherizedException;
import main.exceptions.UsernameAlreadyUsedException;
import main.interfaces.dbInterface;
import main.applicationServer.uno.Card;

public class dbInterfaceImpl extends UnicastRemoteObject implements dbInterface {

	private Database db;
	private List<dbInterface> databaseServers;
	private int portnumber;

	private final int dbPortnumber = 1300;
	private final int NUMBER_OF_DATABASES = 4;

	public dbInterfaceImpl(String uri, int portnumber) throws SQLException, UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException  {
		db = new Database(uri);
		db.createUserTable();
		db.createGameTable();
		db.createImagesTable();
		databaseServers = new ArrayList<>();
		this.portnumber = portnumber;
		System.out.println("----------------------------------------------");

	}

	@Override
	public void addUser(String username, String password) throws RemoteException, UsernameAlreadyUsedException {
        if (!db.checkUsername(username)) throw new UsernameAlreadyUsedException("The username is already used! Please chose another username!");

//        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
//		String token = db.createToken(username, timestamp);
//		db.addUser(username, password, token, timestamp);
//		for (dbInterface database : databaseServers) {
//			database.duplicateAddUser(username, password, token, timestamp);
//		}
	}
	
	@Override
	public void duplicateAddUser(String username, String password, String token, Timestamp timestamp)
			throws RemoteException, InvalidKeyException, SignatureException {
		this.db.addUser(username, password, token, timestamp);
	}

	@Override
	public boolean checkUsername(String username) throws RemoteException {
		return db.checkUsername(username);
	}

	@Override
	public boolean loginUser(String username, String password) throws RemoteException, UnAutherizedException{
        if (!db.checkUsername(username)) throw new UnAutherizedException("Combination of username and password does not exist!");
        return db.loginUser(username, password);
	}

	@Override
	public String getPlayerHand(int user_id) throws RemoteException, SQLException {
		return db.getPlayerHand(user_id);
	}

	@Override
	public void addUsersToGame(String game_name, List<String> users) throws RemoteException {
		List<String> temp = new ArrayList<>();
		temp.addAll(users);
		for (int i = 0; i < 4 - users.size(); i++) {
			temp.add(new String(""));
		}
		int game_id = db.getGameId(game_name);
		db.addUserToGame(game_id, temp.get(0), temp.get(1), temp.get(2), temp.get(3));
		for (dbInterface database : databaseServers) {
			database.duplicateAddUsersToGame(game_id, temp);
		}
	}
	
	@Override
	public void duplicateAddUsersToGame(int game_id, List<String> users) throws RemoteException {
		db.addUserToGame(game_id, users.get(0), users.get(1), users.get(2), users.get(3));
	}
	
	@Override
	public String addGame(int id, String name, int aantalSpelers, int serverport, int theme) {
		String dbID = id + "" + this.portnumber;
		db.addGame(dbID, name, aantalSpelers, serverport, theme);
		for (dbInterface iter : databaseServers) {
			try {
				iter.duplicateAddGame(dbID, name, aantalSpelers, serverport, theme);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		return dbID;
	}
	
	@Override
	public void duplicateAddGame(String id, String name, int aantalSpelers, int serverport, int theme) {
		db.addGame(id, name, aantalSpelers, serverport, theme);
	}

	@Override
	public List<String> getActiveGames() throws RemoteException, SQLException {
		return db.getActiveGames();
	}

	@Override
	public void StopGame(int game_id) throws RemoteException {
		db.StopGame(game_id);
	}

	public Database getDb() {
		return db;
	}

	public void setDb(Database db) {
		this.db = db;
	}

	public List<dbInterface> getDatabaseServers() {
		return databaseServers;
	}

	@Override
	public void setDatabaseServers() {
		for (int i = dbPortnumber; i < dbPortnumber + NUMBER_OF_DATABASES; i++) {
			if (i != this.portnumber) {
				Registry registry;
				try {
					registry = LocateRegistry.getRegistry("localhost", i);
					this.databaseServers.add((dbInterface) registry.lookup("UNOdatabase" + i));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// check the connection
		for (dbInterface databaseInterface : this.databaseServers) {
			databaseInterface.ping(this.portnumber);
		}
	}

	public void addDatabaseServer(dbInterfaceImpl databaseServer) {
		this.databaseServers.add(databaseServer);
	}

	@Override
	public void ping(int portnumber) throws RemoteException {
		System.out.println("server " + portnumber + " has pinged server " + this.portnumber);
	}

	@Override
	public int getPortnumber() throws RemoteException {
		return this.portnumber;
	}

	@Override
	public void updateHandPlayer(String name, List<Card> cards, String dbID) throws RemoteException {
		db.playTurn(name, cards, dbID);
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				for (dbInterface databaseInterface : databaseServers) {
					try {
						databaseInterface.duplicateUpdateHandPlayer(name, cards, dbID);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}				
			}
		});
		thread.start();

	}

	@Override
	public void duplicateUpdateHandPlayer(String name, List<Card> cards, String dbID) throws RemoteException {
		db.playTurn(name, cards, dbID);
	}
	
	@Override
	public void createPlayerHandTabel(String id) throws RemoteException {
		try {
			db.createPlayerHandTable(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		for (dbInterface iter : databaseServers) {
			iter.duplicateCreatePlayerHand(id);
		}
	}
	
	@Override
	public void duplicateCreatePlayerHand(String id) throws RemoteException {
		try {
			db.createPlayerHandTable(id);
		} catch (SQLException e) {
			e.printStackTrace();
		}		
	}

	public String getLoginToken(String username) throws SQLException, RemoteException {
		return db.getToken(username);
	}
}
