package main.client;

import java.io.Serializable;

public class GameInfo implements Serializable {
    private final int gameID;
    private final int gameTheme;
    private final String gameName;
    private final int amountOfPlayers;

    public GameInfo(int gameID, int gameTheme, String gameName, int amountOfPlayers) {
        this.gameID = gameID;
        this.gameTheme = gameTheme;
        this.gameName = gameName;
        this.amountOfPlayers = amountOfPlayers;
    }

    public int getGameID() {
        return gameID;
    }

    public int getGameTheme() {
        return gameTheme;
    }

    public String getGameName() {
        return gameName;
    }

    public int getAmountOfPlayers() {
        return amountOfPlayers;
    }

    public boolean isValid() {
        return gameName!=null && !gameName.isEmpty() && gameID!=0 && amountOfPlayers!=0;
    }
}
