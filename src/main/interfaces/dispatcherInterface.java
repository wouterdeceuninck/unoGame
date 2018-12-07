package main.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface dispatcherInterface extends Remote {
	// user methods
	/**
	 * @return
	 * @throws RemoteException
	 */
	public int getPort() throws RemoteException;

	// server methods
	/**
	 * @param serverPort
	 * @param load
	 * @throws RemoteException
	 */
	public void updateInfo(int serverPort, int load) throws RemoteException;

}
