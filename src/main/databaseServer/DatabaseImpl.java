package databaseServer;

import client.GameInfo;
import databaseServer.businessObjects.GameObject;
import databaseServer.mapper.InfoToObjectMapper;
import databaseServer.mapper.ObjectToInfoMapper;
import databaseServer.security.PasswordVerifier;
import databaseServer.tables.GameTable;
import databaseServer.tables.UserTable;
import exceptions.UnAutherizedException;
import exceptions.UserNotFoundException;
import exceptions.UsernameAlreadyUsedException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseImpl extends UnicastRemoteObject implements databaseServer.DbInterface {
    private static final String URI = "resources\\database";
    private final int portnumber;
    private final UserTable userTable;
    private final GameTable gameTable;
    private final List<DbInterface> replicateDb;

    public DatabaseImpl (int portnumber) throws RemoteException {
        this.portnumber = portnumber;
        this.userTable = new UserTable(URI + portnumber + ".db");
        this.gameTable = new GameTable(URI + portnumber + ".db");
        replicateDb = new ArrayList<>();
    }

    @Override
    public String loginUser(String username, String password) throws UnAutherizedException {
        String token = null;
        try {
            token = userTable.loginUser(username, password);
        } catch (UserNotFoundException e) {
            searchForUserOnOtherDBServers(username, password);
        }
        if (token != null) replicateLoginUser(username, token);
        return token;
    }

    private void searchForUserOnOtherDBServers(String username, String password) {
        replicateDb.stream().map(dbInterface -> {
            try {
                return dbInterface.loginUser(username, password);
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
            return null;
        });
    }

    @Override
    public List<GameInfo> getActiveGames(){
        return gameTable.getActiveGames().stream()
                .map(gameObject -> ObjectToInfoMapper.mapToInfo(gameObject))
                .collect(Collectors.toList());
    }

    @Override
    public void setInactive(String game_id){
        gameTable.setInactive(game_id);
        replicateSetInactive(game_id);
    }

    @Override
    public String registerUser(String username, String password) throws UsernameAlreadyUsedException{
        String hashedAndSalt = PasswordVerifier.createPassword(password);
        String token = userTable.addUser(username, hashedAndSalt);
        replicateAddUser(username, hashedAndSalt, token);
        return token;
    }

    @Override
    public String addGame(GameInfo gameInfo, int serverport){
        GameObject gameObject = InfoToObjectMapper.mapToObject(gameInfo, serverport);
        String game_id = gameTable.addGame(gameObject);
        replicateAddGame(gameObject, game_id);
        return game_id;
    }

    @Override
    public boolean addUsersToGame(String game_id) {
        boolean gameFound = gameTable.addUserToGame(game_id);
        if (gameFound) replicateAddUserToGame(game_id);
        return gameFound;
    }

    @Override
    public void removeUsersFromGame(String game_id){
        gameTable.removeUserFromGame(game_id);
        replicateRemoveUserFromGame(game_id);
    }

    @Override
    public void duplicateSetInactive(String game_id) {
        new Thread(() -> gameTable.setInactive(game_id)).start();
    }

    @Override
    public void duplicateLoginUser(String username, String token) {
        new Thread(() -> userTable.duplicateLoginUser(username, token)).start();
    }

    @Override
    public void duplicateRegisterUser(String username, String password, String token){
        new Thread(() -> userTable.duplicateAddUser(username, password, token)).start();
    }

    @Override
    public void duplicateAddGame(GameObject gameObject, String game_id){
        new Thread(() -> gameTable.duplicateAddGame(gameObject, game_id)).start();
    }

    @Override
    public void duplicateAddUsersToGame(String game_id){
        new Thread(() -> gameTable.addUserToGame(game_id)).start();
    }

    @Override
    public void duplicateRemoveUsersFromGame(String game_id){
        new Thread(() -> gameTable.removeUserFromGame(game_id)).start();
    }

    @Override
    public String getUser_forTestsOnly(String username) throws RemoteException {
        return userTable.getUser(username).toString();
    }

    @Override
    public void connectToDBServer(DbInterface dbInterface){
        replicateDb.add(dbInterface);
    }

    private void replicateSetInactive(String game_id) {
        this.replicateDb.stream().forEach(dbInterface -> {
            try {
                dbInterface.duplicateSetInactive(game_id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private void replicateRemoveUserFromGame(String game_id) {
        this.replicateDb.stream().forEach(dbInterface -> {
            try {
                dbInterface.duplicateRemoveUsersFromGame(game_id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private void replicateLoginUser(String username, String token) {
        this.replicateDb.stream().forEach(dbInterface -> {
            try {
                dbInterface.duplicateLoginUser(username, token);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private void replicateAddUserToGame(String game_id) {
        this.replicateDb.forEach(dbInterface -> {
            try {
                dbInterface.duplicateAddUsersToGame(game_id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private void replicateAddUser(String username, String password, String token) {
        this.replicateDb.stream().forEach(dbInterface -> {
            try {
                dbInterface.duplicateRegisterUser(username, password, token);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private void replicateAddGame(GameObject gameObject, String game_id) {
        this.replicateDb.stream().forEach(dbInterface -> {
            try {
                dbInterface.duplicateAddGame(gameObject, game_id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
    }
}
