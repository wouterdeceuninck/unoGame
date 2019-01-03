package  dispatcher;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import applicationServer.ServerInterfaceImpl;
import databaseServer.DatabaseImpl;
import databaseServer.DbInterface;
import dispatcher.infoObjects.ApplicationServerInfo;
import dispatcher.infoObjects.DatabaseInfo;

public class DispatcherInterfaceImpl extends UnicastRemoteObject implements DispatcherInterface {

	private static final int MAXSERVERLOAD = 20;
	private List<ApplicationServerInfo> applicationServers;
	private List<DatabaseInfo> databaseServers;

	private int serverPort = 1200;
	private int dbPortnumber = 1300;
	private final int NUMBER_OF_DATABASES = 4;

	private String uri = ".\\main.applicationServer.uno.db";

	public DispatcherInterfaceImpl() throws IOException {
		applicationServers = new ArrayList<>();
		databaseServers = new ArrayList<>();
	}

	private void makeConnect(DbInterface newDBServer) throws RemoteException {
		databaseServers.forEach(databaseInfo -> {
			try {
				databaseInfo.dbInterface.connectToDBServer(newDBServer);
				newDBServer.connectToDBServer(databaseInfo.dbInterface);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		});
	}

	private int createOneServer() {
		try {
			int dbPortnumber = getDBServerPortnumber();
			System.out.println("Least Loaded: " + dbPortnumber);
			Registry registry = LocateRegistry.createRegistry(serverPort);
			ServerInterfaceImpl server = new ServerInterfaceImpl(serverPort, dbPortnumber);
			registry.bind("UNOserver", server);

			// update class variables
			applicationServers.add(new ApplicationServerInfo(serverPort, server));

		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}
		return serverPort++;
	}

	// give uri => location on disk
	private int createDbServer(int portnumber) {
		Registry registry;
		try {
			registry = LocateRegistry.createRegistry(portnumber);
			DbInterface dbInterface = new DatabaseImpl(portnumber);
			registry.bind("UNOdatabase" + (portnumber), dbInterface);
			makeConnect(dbInterface);
			databaseServers.add(new DatabaseInfo(dbInterface, dbPortnumber, registry));
		} catch (RemoteException | AlreadyBoundException e) {
			e.printStackTrace();
		}
		return dbPortnumber++;
	}

	@Override
	public int getLeastLoadedApplicationServer() {
		ApplicationServerInfo applicationServerInfo = applicationServers.stream()
				.sorted(Comparator.comparingInt(this::getAmountOfGames))
				.findFirst()
				.get();
		if (getAmountOfGames(applicationServerInfo) >= MAXSERVERLOAD) return createOneServer();
		return applicationServerInfo.portnumber;
	}

	@Override
	public int createApplicationServer() {
		return createOneServer();
	}

	@Override
	public int createDBServer() {
		return createDbServer(dbPortnumber);
	}

	@Override
	public int getDBServerPortnumber() {
		Collections.shuffle(databaseServers);
		return databaseServers.get(0).portnumber;
	}

	private Integer getAmountOfGames(ApplicationServerInfo applicationServerInfo) {
		try {
			return applicationServerInfo.serverInterface.getAmountOfGameOnServer();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}
}
