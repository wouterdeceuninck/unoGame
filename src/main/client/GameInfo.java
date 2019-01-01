package client;

import java.io.Serializable;

public class GameInfo implements Serializable {
    private String gameID;
    private final int gameTheme;
    private final String gameName;
    private final int amountOfPlayers;
    private final int connectedPlayers;
    private int serverPortnumber;

    public GameInfo(String gameID, String gameName, int amountOfPlayers, int connectedPlayers, int gameTheme) {
        this.gameID = gameID;
        this.gameTheme = gameTheme;
        this.gameName = gameName;
        this.amountOfPlayers = amountOfPlayers;
        this.connectedPlayers = connectedPlayers;
    }

    public int getConnectedPlayers() {
        return connectedPlayers;
    }

    public void setGameID(String gameID) {
        this.gameID = gameID;
    }

    public String getGameID() {
        return gameID;
    }

    public int getServerPortnumber() {
        return serverPortnumber;
    }

    public void setServerPortnumber(int serverPortnumber) {
        this.serverPortnumber = serverPortnumber;
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

    @Override
    public String toString() {
        return this.getGameID() + "\t" + this.gameName + "\t" + this.connectedPlayers + "/" + this.amountOfPlayers + "\t" + this.gameTheme;
    }
    public static class Builder{
        private String gameID;
        private int gameTheme;
        private String gameName;
        private int amountOfPlayers;
        private int connectedPlayers;

        public Builder setConnectedPlayers(int connectedPlayers) {
            this.connectedPlayers = connectedPlayers;
            return this;
        }

        public Builder setGameID(String gameID) {
            this.gameID = gameID;
            return this;
        }

        public Builder setGameTheme(int gameTheme) {
            this.gameTheme = gameTheme;
            return this;
        }

        public Builder setGameName(String gameName) {
            this.gameName = gameName;
            return this;
        }

        public Builder setAmountOfPlayers(int amountOfPlayers) {
            this.amountOfPlayers = amountOfPlayers;
            return this;
        }

        public GameInfo build() {
            return new GameInfo(this.gameID, this.gameName, this.amountOfPlayers, this.connectedPlayers, this.gameTheme);
        }
    }
}
