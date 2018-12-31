package dispatcher;

import applicationServer.ServerInterface;
import client.GameInfo;
import databaseServer.DbInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface DispatcherInterface extends Remote {
    ServerInterface getLeastLoadedApplicationServer() throws RemoteException;
    int getDBServerPortnumber() throws RemoteException;
    int createApplicationServer() throws RemoteException;
    int createDBServer() throws RemoteException;
}
