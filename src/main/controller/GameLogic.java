package main.controller;

import main.client.GameData;
import main.client.GameInfo;
import main.client.UserController;
import main.exceptions.CardNotFoundException;
import main.interfaces.ServerInterface;
import main.uno.Card;

import java.rmi.RemoteException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class GameLogic {
    private UserController userController;
    private GameData gameData;

    public GameLogic(UserController userController) {
        this.userController = userController;
        this.gameData = new GameData();
    }

    public void sendGameMsg(String text) {
        try {
            userController.sendGameMsg(userController, text);
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
        return userController.getGameInfo();
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
