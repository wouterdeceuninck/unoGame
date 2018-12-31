package applicationServer.uno.player;

import applicationServer.uno.cards.properties.CardColours;
import applicationServer.uno.cards.properties.CardSymbol;
import applicationServer.uno.cards.Card;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class BotPlayer implements PlayerInterface {
    private final String name;
    private StringBuilder botLog;
    private List<Card> cards;
    private Deque<Card> pile;
    private boolean ready;

    public BotPlayer(String name){
        ready = true;
        this.botLog = new StringBuilder();
        this.name = name;
        this.cards = new ArrayList<>();
        this.pile = new ArrayDeque<>();
    }

    @Override
    public void sendMessage(String message) {
        botLog.append(message);
    }

    @Override
    public void setCardAmountPlayer(String name, int size) {
//        botLog.append(name + " has " + size + " cards.\n");
    }

    @Override
    public void addCards(List<Card> cards) {
        this.cards.addAll(cards);
    }

    @Override
    public void addPile(Card card) {
        this.pile.push(card);
    }

    @Override
    public Card getCard() {
        Card pickedCard = this.cards.stream()
                .filter(card -> card.canPlayOn(pile.peek()))
                .findAny()
                .orElse(null);
        Card card = pickedCard == null ? null : setColor(pickedCard);
        System.out.println(getLogMessage(card));
        return card;
    }

    private String getLogMessage(Card card) {
        return card == null ? this.name + "picks a card!" : this.name + ": played the card " + card.toString() + "\n"
                + "\tAnd has " + this.cards.size() + " cards left!";
    }

    private Card setColor(Card pickedCard) {
        boolean isBlackCard = pickedCard.mySymbol == CardSymbol.WILDDRAWCARD || pickedCard.mySymbol == CardSymbol.WILDCARD;
        return isBlackCard ? setColorOfBlackCard(pickedCard) : pickedCard;
    }

    private Card setColorOfBlackCard(Card card) {
        card.myColour = CardColours.BLUE;
        return card;
    }

    @Override
    public List<Card> getCards() {
        return this.cards;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setScore() {
        // TODO
    }

    @Override
    public boolean isReady() {
        return this.ready;
    }

    @Override
    public void setPlayerNotReady() {
        this.ready = false;
    }

    @Override
    public CardColours askColor() {
        return CardColours.BLUE;
    }

    @Override
    public String getLog() {
        return this.botLog.toString();
    }

    @Override
    public Deque<Card> getPile() {
        return this.pile;
    }
}
