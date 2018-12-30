package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface lobbyInterface extends Remote {

	/**
	 * @param msg
	 * @throws RemoteException
	 */
	public void setMsg(String msg) throws RemoteException;

}
