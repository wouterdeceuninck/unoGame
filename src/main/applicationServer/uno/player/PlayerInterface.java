package applicationServer.uno.player;

import applicationServer.uno.cards.Card;
import applicationServer.uno.cards.properties.CardColours;

import java.util.Deque;
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
    String getLog();
    Deque<Card> getPile();
}