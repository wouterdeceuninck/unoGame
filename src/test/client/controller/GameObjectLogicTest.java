package test.client.controller;

import main.applicationServer.uno.CardColours;
import main.applicationServer.uno.CardSymbol;
import main.client.controller.GameLogic;
import main.exceptions.CardNotFoundException;
import main.applicationServer.uno.Card;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class GameObjectLogicTest {
    private GameLogic gameLogic = new GameLogic(null);

    @Test
    public void addCardsToGameLogic() {
        gameLogic.addCards(getDummyCards());
        Card pickedCard = new Card(CardColours.BLUE, CardSymbol.ONE);
        gameLogic.removeCard(pickedCard);
        Assert.assertFalse(gameLogic.getGameData().getCards().contains(pickedCard));
    }

    @Test(expected = CardNotFoundException.class)
    public void addCardsToGameLogic_expectException() {
        gameLogic.addCards(getDummyCards());
        Card pickedCard = new Card(CardColours.BLUE, CardSymbol.NULL);
        gameLogic.removeCard(pickedCard);
    }

    private List<Card> getDummyCards() {
        List<Card> dummyCards = new ArrayList<>();
        dummyCards.add(new Card(CardColours.BLUE, CardSymbol.TWO));
        dummyCards.add(new Card(CardColours.RED, CardSymbol.ONE));
        dummyCards.add(new Card(CardColours.GREEN, CardSymbol.EIGHT));
        dummyCards.add(new Card(CardColours.YELLOW, CardSymbol.SEVEN));
        dummyCards.add(new Card(CardColours.YELLOW, CardSymbol.NINE));
        return dummyCards;
    }
}