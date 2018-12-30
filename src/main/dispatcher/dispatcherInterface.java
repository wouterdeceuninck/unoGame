package dispatcher;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface dispatcherInterface extends Remote {
	int getPort() throws RemoteException;
}
