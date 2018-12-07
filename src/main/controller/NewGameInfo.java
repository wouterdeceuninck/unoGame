package main.controller;

import java.io.Serializable;

public class NewGameInfo implements Serializable {

    private final String gameName;
    private final Integer theme;
    private final Integer numberOfPlayers;

    public NewGameInfo(String gameName, Integer theme, Integer numberOfPlayers) {
        this.gameName = gameName;
        this.theme = theme;
        this.numberOfPlayers = numberOfPlayers;
    }

    public String getGameName() {
        return gameName;
    }

    public Integer getTheme() {
        return theme;
    }

    public Integer getNumberOfPlayers() {
        return numberOfPlayers;
    }
}
