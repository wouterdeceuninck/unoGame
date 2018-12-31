package dispatcher;

import databaseServer.DbInterface;

import java.rmi.registry.Registry;

public class DatabaseInfo {
    public Registry registry;
    public DbInterface dbInterface;
    public int portnumber;

    public DatabaseInfo(DbInterface dbInterface, int portnumber, Registry registry) {
        this.dbInterface = dbInterface;
        this.portnumber = portnumber;
        this.registry = registry;
    }
}
