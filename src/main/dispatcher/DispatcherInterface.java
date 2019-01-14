package dispatcher;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DispatcherInterface extends Remote {
    /**
     * Returns the least loaded applicationserver portnumber
     */
    int getLeastLoadedApplicationServer() throws RemoteException;
    /**
     * Returns one of the databaseserver portnumber
     */
    int getDBServerPortnumber() throws RemoteException;
    /**
     * Create a new applicationserver and returns ths portnumber
     */
    int createApplicationServer() throws RemoteException;
    /**
     * Create a new databaseserver and returns the portnumber
     */
    int createDBServer() throws RemoteException;
}
