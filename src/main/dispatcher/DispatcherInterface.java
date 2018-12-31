package dispatcher;

import client.GameInfo;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DispatcherInterface extends Remote {
    int getLeastLoadedApplicationServer() throws RemoteException;
    void createApplicationServer() throws RemoteException;
    void createDBServer() throws RemoteException;
}
