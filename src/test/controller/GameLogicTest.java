package test.controller;

import main.applicationServer.uno.CardColours;
import main.controller.GameLogic;
import main.exceptions.CardNotFoundException;
import main.applicationServer.uno.Card;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GameLogicTest {
    private GameLogic gameLogic = new GameLogic(null);

    @Test
    public void addCardsToGameLogic() {
        gameLogic.addCards(getDummyCards());
        Card pickedCard = new Card(CardColours.BLUE, 1);
        gameLogic.removeCard(pickedCard);
        Assert.assertFalse(gameLogic.getGameData().getCards().contains(pickedCard));
    }

    @Test(expected = CardNotFoundException.class)
    public void addCardsToGameLogic_expectException() {
        gameLogic.addCards(getDummyCards());
        Card pickedCard = new Card(CardColours.BLUE, 0);
        gameLogic.removeCard(pickedCard);
    }

    private List<Card> getDummyCards() {
        List<Card> dummyCards = new ArrayList<>();
        dummyCards.add(new Card(CardColours.BLUE, 2));
        dummyCards.add(new Card(CardColours.RED, 1));
        dummyCards.add(new Card(CardColours.GREEN, 8));
        dummyCards.add(new Card(CardColours.YELLOW, 7));
        dummyCards.add(new Card(CardColours.YELLOW, 9));
        return dummyCards;
    }
}