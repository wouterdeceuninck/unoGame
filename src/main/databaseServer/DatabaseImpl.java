package databaseServer;

import client.GameInfo;
import databaseServer.mapper.InfoToObjectMapper;
import databaseServer.mapper.ObjectToInfoMapper;
import databaseServer.tables.GameTable;
import databaseServer.tables.UserTable;
import exceptions.UnAutherizedException;
import exceptions.UsernameAlreadyUsedException;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class DatabaseImpl extends UnicastRemoteObject implements databaseServer.DbInterface {
    private static final String URI = "src\\main\\resources\\database";
    private final int portnumber;
    private final UserTable userTable;
    private final GameTable gameTable;

    public DatabaseImpl (int portnumber) throws RemoteException {
        this.portnumber = portnumber;
        this.userTable = new UserTable(URI + portnumber + ".db");
        this.gameTable = new GameTable(URI + portnumber + ".db");
    }

    @Override
    public void interconnectDBServers() {

    }

    @Override
    public String loginUser(String username, String password) throws UnAutherizedException {
        return userTable.loginUser(username, password);
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
    }

    @Override
    public String registerUser(String username, String password) throws UsernameAlreadyUsedException{
        return userTable.addUser(username, password);
    }

    @Override
    public void duplicateAddUser(String username, String password, String token, Timestamp timestamp){

    }

    @Override
    public String addGame(GameInfo gameInfo, int serverport){
        return gameTable.addGame(InfoToObjectMapper.mapToObject(gameInfo, serverport));
    }

    @Override
    public void duplicateAddGame(GameInfo gameInfo, int serverport){

    }

    @Override
    public boolean addUsersToGame(String game_id) {
        return gameTable.addUserToGame(game_id);
    }

    @Override
    public void removeUsersFromGame(String game_id){
        gameTable.removeUserFromGame(game_id);
    }

    @Override
    public void duplicateAddUsersToGame(String game_id){

    }

    @Override
    public void duplicateremoveUsersFromGame(String game_id){

    }

    @Override
    public Integer getAmountOfServerConnections() {
        return null;
    }
}
