package main.client;

public class GameInfo {
    private final int gameID;
    private final int gameTheme;
    private final String gameName;

    public GameInfo(int gameID, int gameTheme, String gameName) {
        this.gameID = gameID;
        this.gameTheme = gameTheme;
        this.gameName = gameName;
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
}
