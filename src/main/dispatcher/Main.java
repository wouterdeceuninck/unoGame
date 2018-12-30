package dispatcher;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main {

	public void startServer() {
		try {
			Registry registry = LocateRegistry.createRegistry(1099);
			dispatcherInterfaceImpl d = new dispatcherInterfaceImpl();
			registry.rebind("UNOdispatcher", d);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new Main().startServer();
	}

}
