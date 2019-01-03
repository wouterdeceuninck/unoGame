package databaseServer.mapper;

import client.businessObjects.GameInfo;
import databaseServer.businessObjects.GameObject;

public class InfoToObjectMapper {

    public static GameObject mapToObject(GameInfo gameInfo, int serverport) {
        return new GameObject.Builder()
                .setUUID(gameInfo.getGameID())
                .setGameName(gameInfo.getGameName())
                .setAmountOfPlayers(gameInfo.getAmountOfPlayers())
                .setConnectedPlayers(0)
                .setActive(true)
                .setServerport(serverport)
                .buildGame();
    }
}
