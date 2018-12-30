package databaseServer.mapper;

import client.GameInfo;
import databaseServer.businessObjects.GameObject;

public class ObjectToInfoMapper {

    public static GameInfo mapToInfo(GameObject gameObject) {
        return new GameInfo.Builder()
                .setGameID(gameObject.getID())
                .setGameName(gameObject.getName())
                .setAmountOfPlayers(gameObject.getAmountOfPlayers())
                .setConnectedPlayers(gameObject.getConnectedPlayers())
                .build();
    }
}