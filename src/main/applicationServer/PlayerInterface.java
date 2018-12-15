package main.applicationServer;

import main.applicationServer.uno.Card;
import main.applicationServer.uno.CardColours;

import java.util.List;

public interface PlayerInterface {
    void sendMessage(String message);
    void setCardAmountPlayer(String name, int size);
    void addCards(List<Card> cards);
    void addPile(Card card);
    Card getCard();
    List<Card> getCards();
    String getName();
    void setScore();
    boolean isReady();
    void setPlayerNotReady();
    CardColours askColor();
    String getLog();
}