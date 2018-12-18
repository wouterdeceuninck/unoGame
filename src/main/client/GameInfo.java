package main.client;

import java.io.Serializable;

public class GameInfo implements Serializable {
    private final String gameID;
    private final int gameTheme;
    private final String gameName;
    private final int amountOfPlayers;

    public GameInfo(String gameID, int gameTheme, String gameName, int amountOfPlayers) {
        this.gameID = gameID;
        this.gameTheme = gameTheme;
        this.gameName = gameName;
        this.amountOfPlayers = amountOfPlayers;
    }

    public String getGameID() {
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
        return gameName!=null && !gameName.isEmpty() && !gameID.isEmpty() && amountOfPlayers!=0;
    }
}
