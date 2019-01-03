package dispatcher;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DispatcherInterface extends Remote {
    int getLeastLoadedApplicationServer() throws RemoteException;
    int getDBServerPortnumber() throws RemoteException;
    int createApplicationServer() throws RemoteException;
    int createDBServer() throws RemoteException;
}
