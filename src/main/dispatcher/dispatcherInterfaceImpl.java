package dispatcher;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import applicationServer.ServerInterfaceImpl;
//import databaseServer.dbInterfaceImpl;
import databaseServer.DatabaseImpl;
import databaseServer.DbInterface;

public class dispatcherInterfaceImpl extends UnicastRemoteObject implements dispatcherInterface {

	private Map<Integer, Integer> serverStatus, dbServerStatus;
	private Set<Integer> unfilledServers;
	private Set<Integer> fullServers;
	private List<DbInterface> databaseServers;
	private int serverPort;

	// private String uri
	private String uri = ".\\main.applicationServer.uno.db";

	private Map<Integer, Integer> serverToDB;

	private final int dbPortnumber = 1300;
	private final int NUMBER_OF_DATABASES = 4;


	public dispatcherInterfaceImpl() throws SQLException, UnrecoverableKeyException,
			KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {
		serverStatus = new HashMap<>();
		dbServerStatus = new HashMap<>();
		unfilledServers = new HashSet<>();
		fullServers = new HashSet<>();
		serverToDB = new HashMap<>();
		databaseServers = new ArrayList<>();
		serverPort = 1200;

		// auto create dbserver
		createDbServers(1300);

		// connect to all the db
		connectToDb();

		// make All dbservers to connect to eachother
		makeConnect();

		// auto create 1 server
		createServer();
		createServer();
		createServer();
		createServer();

	}

	private void makeConnect() throws RemoteException {
		for (DbInterface iter : databaseServers) {
				iter.interconnectDBServers();
		}
	}

	private void connectToDb() {
		for (int i = dbPortnumber; i < dbPortnumber + NUMBER_OF_DATABASES; i++) {
			try {
				Registry registry = LocateRegistry.getRegistry("localhost", i);
				DbInterface tempDB = (DbInterface) registry.lookup("UNOdatabase" + i);
				databaseServers.add(tempDB);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void createServer() {
		int dbPortnumber;
		try {
			dbPortnumber = getLeastLoadedDB();
			System.out.println(dbPortnumber);
			Registry registry = LocateRegistry.createRegistry(serverPort);
			registry.bind("UNOserver", new ServerInterfaceImpl(serverPort, dbPortnumber));

			// update class variables
			unfilledServers.add(serverPort);
			serverStatus.put(serverPort, 0);
			serverPort++;
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}

	}

	// give uri => location on disk
	private void createDbServers(int portnumber) {
		Registry registry = null;
		try {
			registry = LocateRegistry.createRegistry(portnumber);
			DbInterface dbInterface = new DatabaseImpl(portnumber);
			registry.bind("UNOdatabase" + (portnumber), dbInterface);
		} catch (RemoteException | AlreadyBoundException e) {
			e.printStackTrace();
		}
	}

	private int getLeastLoadedDB() throws RemoteException {
		int highestLoad = Integer.MAX_VALUE;
		int portnumber = 1300;
		for (int i = dbPortnumber; i < dbPortnumber + dbServerStatus.size(); i++) {
			if (dbServerStatus.get(i) < highestLoad) {
				highestLoad = dbServerStatus.get(i);
				portnumber = i;
			}
		}
		dbServerStatus.put(portnumber, highestLoad + 1);
		return portnumber;
	}

	@Override
	public int getPort() throws RemoteException {
		int leastLoaded = Integer.MAX_VALUE;
		int portnumber = 1200;
		for (Integer iter : serverStatus.keySet()) {
			if (serverStatus.get(iter) < leastLoaded) {
				leastLoaded = serverStatus.get(iter);
				portnumber = iter;
			}
		}
		return portnumber;

	}
}
