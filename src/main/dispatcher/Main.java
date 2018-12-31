package  dispatcher;

import dispatcher.DispatcherInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

	public void startServer(int amountOfApplicationServers) {
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			DispatcherInterface dispatcherInterface = new DispatcherInterfaceImpl(amountOfApplicationServers);
			registry.rebind("UNOdispatcher", dispatcherInterface);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Main().startServer(10);
	}

}
