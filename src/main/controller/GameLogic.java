package main.controller;

import main.client.GameData;
import main.client.GameInfo;
import main.exceptions.CardNotFoundException;
import main.interfaces.ServerInterface;
import main.uno.Card;

import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameLogic {
    private final String username;
    private ServerInterface server;
    private GameInfo gameInfo;
    private GameData gameData;

    public GameLogic(ServerInterface server, GameInfo gameInfo, String username) {
        this.server = server;
        this.gameInfo = gameInfo;
        this.gameData = new GameData();
        this.username = username;
    }

    public void sendGameMsg(String text) {
        try {
            server.sendGameMsg(text, gameInfo.getGameID(), username);
        } catch (RemoteException e) {
            Logger.getLogger("logger").log(Level.SEVERE, "Remote error exception!");
        }
    }

    public void addCards(List<Card> cards) {
        gameData.addCards(cards);
    }

    public GameData getGameData() {
        return gameData;
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public void removeCard(Card pickedCard) throws CardNotFoundException{

        gameData.removeCard(gameData.getCards().stream()
                .filter(card -> card.toString().equals(pickedCard.toString()))
                .findFirst()
                .orElseThrow(() -> new CardNotFoundException("Card was not present in the players hand!")));
    }

    public void startGame(String username) {
//        try {
//            server.readyToStart(gameInfo.getGameID(), username, gameInfo.getGameTheme());
//        } catch (RemoteException e) {
//            Logger.getLogger("logger").log(Level.SEVERE, "Remote error exception!");
//        }
    }

    public void setTopCard(Card topCard) {
        this.gameData.setTopCard(topCard);
    }
}
