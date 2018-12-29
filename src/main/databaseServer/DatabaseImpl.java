package main.databaseServer;

import main.client.GameInfo;
import main.databaseServer.tables.UserTable;
import main.exceptions.UnAutherizedException;
import main.exceptions.UsernameAlreadyUsedException;

import java.rmi.RemoteException;
import java.sql.Timestamp;
import java.util.List;

public class DatabaseImpl implements  dbInterface {
    static final String URI = "";
    private final int portnumber;
    private final UserTable userTable;

    public DatabaseImpl (int portnumber) {
        this.portnumber = portnumber;
        this.userTable = new UserTable(URI);
    }

    @Override
    public void interconnectDBServers() {

    }

    @Override
    public void checkUsername(String username) throws UsernameAlreadyUsedException {
        userTable.checkUsername(username);
    }

    @Override
    public boolean loginUser(String username, String password) throws UnAutherizedException {
        return userTable.loginUser(username, password);
    }

    @Override
    public List<GameInfo> getActiveGames(){
        return null;
    }

    @Override
    public void setInactive(String game_id){

    }

    @Override
    public void addUser(String username, String password) throws UsernameAlreadyUsedException {
        userTable.addUser(username, password);
    }

    @Override
    public void duplicateAddUser(String username, String password, String token, Timestamp timestamp) throws RemoteException {

    }

    @Override
    public String addGame(String id, String name, int aantalSpelers, int serverport, int theme) throws RemoteException {
        return null;
    }

    @Override
    public void duplicateAddGame(String id, String name, int aantalSpelers, int serverport, int theme) throws RemoteException {

    }

    @Override
    public void addUsersToGame(String game_name, List<String> users) throws RemoteException {

    }

    @Override
    public void duplicateAddUsersToGame(String game_id, List<String> users) throws RemoteException {

    }
}
