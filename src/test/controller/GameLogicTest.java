package test.controller;

import main.controller.GameLogic;
import main.exceptions.CardNotFoundException;
import main.uno.Card;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class GameLogicTest {
    private GameLogic gameLogic = new GameLogic(null, null, null);

    @Test
    public void addCardsToGameLogic() {
        gameLogic.addCards(getDummyCards());
        Card pickedCard = new Card(1, 1);
        gameLogic.removeCard(pickedCard);
        Assert.assertFalse(gameLogic.getGameData().getCards().contains(pickedCard));
    }

    @Test(expected = CardNotFoundException.class)
    public void addCardsToGameLogic_expectException() {
        gameLogic.addCards(getDummyCards());
        Card pickedCard = new Card(1, 0);
        gameLogic.removeCard(pickedCard);
    }

    private List<Card> getDummyCards() {
        List<Card> dummyCards = new ArrayList<>();
        dummyCards.add(new Card(2, 2));
        dummyCards.add(new Card(1, 1));
        dummyCards.add(new Card(1, 8));
        dummyCards.add(new Card(3, 7));
        dummyCards.add(new Card(4, 9));
        return dummyCards;
    }
}