package  dispatcher;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import applicationServer.ServerInterface;
import applicationServer.ServerInterfaceImpl;
import databaseServer.DatabaseImpl;
import databaseServer.DbInterface;

public class DispatcherInterfaceImpl extends UnicastRemoteObject implements DispatcherInterface {

	private List<ServerInterface> applicationServers;
	private List<DbInterface> databaseServers;

	private int serverPort = 1200;
	private int dbPortnumber = 1300;
	private final int NUMBER_OF_DATABASES = 4;

	private String uri = ".\\main.applicationServer.uno.db";

	public DispatcherInterfaceImpl(int amountOfApplicationServers) throws IOException {
		applicationServers = new ArrayList<>();
		databaseServers = new ArrayList<>();
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

	private void createServer(int amountOfApplicationServers) {
		for (int i = 0; i<amountOfApplicationServers; i++) {
			createOneServer();
		}
	}

	private void createOneServer() {
		try {
			int dbPortnumber = getLeastLoadedDBServerPortnumber();
			System.out.println(dbPortnumber);
			Registry registry = LocateRegistry.createRegistry(serverPort);
			ServerInterfaceImpl server = new ServerInterfaceImpl(serverPort, dbPortnumber);
			registry.bind("UNOserver", server);

			// update class variables
			applicationServers.add((ServerInterface) server);
			serverPort++;
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (AlreadyBoundException e) {
			e.printStackTrace();
		}
	}

	// give uri => location on disk
	private void createDbServer(int portnumber) {
		Registry registry = null;
		try {
			registry = LocateRegistry.createRegistry(portnumber);
			DbInterface dbInterface = new DatabaseImpl(portnumber);
			registry.bind("UNOdatabase" + (portnumber), dbInterface);
		} catch (RemoteException | AlreadyBoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getLeastLoadedApplicationServer() {
		return applicationServers.stream()
				.map(this::getAmountOfGames)
				.sorted()
				.findFirst()
				.get();
	}

	@Override
	public void createApplicationServer() {
		createOneServer();
	}

	@Override
	public void createDBServer() {
		createDbServer(dbPortnumber);
		dbPortnumber ++;
	}

	private int getLeastLoadedDBServerPortnumber() {
		return 1300;
	}

	private Integer getAmountOfGames(ServerInterface serverInterface) {
		try {
			return serverInterface.getGames().size();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return null;
	}
}
