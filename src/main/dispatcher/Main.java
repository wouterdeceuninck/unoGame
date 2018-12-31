package  dispatcher;

import dispatcher.DispatcherInterface;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

	public void startServer() {
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			DispatcherInterface dispatcherInterface = new DispatcherInterfaceImpl();
			registry.rebind("UNOdispatcher", dispatcherInterface);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Main().startServer();
	}

}
