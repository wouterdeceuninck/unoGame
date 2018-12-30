package client;

import exceptions.CardIsNotPresentException;
import applicationServer.uno.cards.Card;

import java.util.ArrayList;
import java.util.List;

public class GameData {
    private final List<Card> cards;
    private Card topCard;

    public GameData() {
        this.cards = new ArrayList<>();
    }

    public List<Card> playCard(Card card) throws CardIsNotPresentException {
        if (cards.remove(card)) return cards;
        throw new CardIsNotPresentException("The card you picked is not present!");
    }

    public List<Card> addCards(List<Card> cardsToAdd) {
        this.cards.addAll(cardsToAdd);
        return this.cards;
    }

    public Card getTopCard() {
        return topCard;
    }

    public void setTopCard(Card topCard) {
        this.topCard = topCard;
    }

    public List<Card> getCards() {
        return cards;
    }

    public void removeCard(Card pickedCard) {
        this.cards.remove(pickedCard);
    }
}

