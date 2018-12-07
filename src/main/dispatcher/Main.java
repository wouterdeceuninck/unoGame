package main.dispatcher;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

	/**
	 * 
	 */
	private void startServer() {
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			dispatcherInterfaceImpl d = new dispatcherInterfaceImpl();
			registry.rebind("UNOdispatcher", d);
			System.out.println("chilloutssss");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Dispatcher is ready!");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Main main = new Main();
		main.startServer();
	}

}
