package main.applicationServer.uno;

import main.applicationServer.PlayerInterface;

import java.util.ArrayList;
import java.util.List;

public class BotPlayer implements PlayerInterface {
    private final String name;
    private StringBuilder botLog;
    private List<Card> cards;
    private List<Card> pile;
    private boolean ready;

    public BotPlayer(String name){
        ready = true;
        this.botLog = new StringBuilder();
        this.name = name;
        this.cards = new ArrayList<>();
        this.pile = new ArrayList<>();
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
        this.pile.add(0, card);
        botLog.append("Card played: " + card.toString() + "\n");
    }

    @Override
    public Card getCard() {
        Card pickedCard = this.cards.stream()
                .filter(card -> pile.get(0).canPlayOn(card))
                .findAny()
                .orElse(null);
        botLog.append("Amount of cards left: " + cards.size() + "\n");
        return pickedCard;
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
}
