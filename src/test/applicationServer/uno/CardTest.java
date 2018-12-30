package applicationServer.uno;

import applicationServer.uno.cards.*;
import applicationServer.uno.cards.properties.CardColours;
import applicationServer.uno.cards.properties.CardSymbol;
import org.junit.Assert;
import org.junit.Test;

public class CardTest {
    private Card yellowOne;
    private Card blueTwo;
    private Card redThree;
    private Card greenFour;
    private Card yellowFive;
    private Card blueSix;
    private Card redSeven;
    private Card greenEight;

    @Test
    public void canPlayOn_expectAllowed() {
        initStartingDeck();

        Assert.assertTrue(yellowOne.canPlayOn(yellowFive));
        Assert.assertTrue(blueTwo.canPlayOn(blueSix));
        Assert.assertTrue(redThree.canPlayOn(redSeven));
        Assert.assertTrue(greenFour.canPlayOn(greenEight));
        Assert.assertTrue(yellowFive.canPlayOn(yellowOne));
        Assert.assertTrue(blueSix.canPlayOn(blueTwo));
        Assert.assertTrue(redSeven.canPlayOn(redThree));
        Assert.assertTrue(greenEight.canPlayOn(greenFour));
    }

    @Test
    public void canPlayOn_expectNotAllowed() {
        initStartingDeck();

        Assert.assertFalse(yellowOne.canPlayOn(blueSix));
        Assert.assertFalse(blueTwo.canPlayOn(redSeven));
        Assert.assertFalse(redThree.canPlayOn(greenEight));
        Assert.assertFalse(greenFour.canPlayOn(yellowFive));
        Assert.assertFalse(yellowFive.canPlayOn(blueSix));
        Assert.assertFalse(blueSix.canPlayOn(redSeven));
        Assert.assertFalse(redSeven.canPlayOn(greenEight));
        Assert.assertFalse(greenEight.canPlayOn(yellowFive));
    }

    @Test
    public void canPlayOn_expectAllowed_specialCards() {
        initStartingDeck();
        Card yellowReverseCard = new ReverseCard(CardColours.YELLOW);
        Card yellowDrawCard = new DrawCard(CardColours.YELLOW, 2);
        Card yellowSkipCard = new SkipCard(CardColours.YELLOW, 1);
        Card blueReverseCard = new ReverseCard(CardColours.BLUE);
        Card blueDrawCard = new DrawCard(CardColours.BLUE, 2);
        Card blueSkipCard = new SkipCard(CardColours.BLUE, 1);

        Assert.assertTrue(yellowDrawCard.canPlayOn(yellowFive));
        Assert.assertTrue(yellowReverseCard.canPlayOn(yellowFive));
        Assert.assertTrue(yellowSkipCard.canPlayOn(yellowFive));
        Assert.assertTrue(yellowFive.canPlayOn(yellowDrawCard));
        Assert.assertTrue(yellowFive.canPlayOn(yellowReverseCard));
        Assert.assertTrue(yellowFive.canPlayOn(yellowSkipCard));
        Assert.assertTrue(yellowSkipCard.canPlayOn(yellowDrawCard));
        Assert.assertTrue(yellowDrawCard.canPlayOn(yellowReverseCard));
        Assert.assertTrue(yellowReverseCard.canPlayOn(yellowSkipCard));
        Assert.assertTrue(yellowDrawCard.canPlayOn(blueDrawCard));
        Assert.assertTrue(yellowSkipCard.canPlayOn(blueSkipCard));
        Assert.assertTrue(yellowReverseCard.canPlayOn(blueReverseCard));
    }

    @Test
    public void canPlayOn_expectNotAllowed_specialCards() {
        initStartingDeck();
        Card yellowReverseCard = new ReverseCard(CardColours.YELLOW);
        Card yellowDrawCard = new DrawCard(CardColours.YELLOW, 2);
        Card yellowSkipCard = new SkipCard(CardColours.YELLOW, 1);
        Card blueReverseCard = new ReverseCard(CardColours.BLUE);
        Card blueDrawCard = new DrawCard(CardColours.BLUE, 2);
        Card blueSkipCard = new SkipCard(CardColours.BLUE, 1);

        Assert.assertFalse(yellowDrawCard.canPlayOn(redSeven));
        Assert.assertFalse(yellowReverseCard.canPlayOn(redSeven));
        Assert.assertFalse(yellowSkipCard.canPlayOn(redSeven));
        Assert.assertFalse(blueDrawCard.canPlayOn(yellowFive));
        Assert.assertFalse(blueDrawCard.canPlayOn(yellowReverseCard));
        Assert.assertFalse(blueDrawCard.canPlayOn(yellowSkipCard));
        Assert.assertFalse(yellowSkipCard.canPlayOn(redSeven));
        Assert.assertFalse(yellowDrawCard.canPlayOn(blueReverseCard));
        Assert.assertFalse(yellowReverseCard.canPlayOn(blueDrawCard));
        Assert.assertFalse(yellowSkipCard.canPlayOn(blueSix));
        Assert.assertFalse(yellowReverseCard.canPlayOn(blueSkipCard));
        Assert.assertFalse(yellowDrawCard.canPlayOn(blueReverseCard));
    }

    @Test
    public void canPlayOn_expectAllowed_blackCards() {
        initStartingDeck();
        Card blueReverseCard = new ReverseCard(CardColours.BLUE);
        Card blueDrawCard = new DrawCard(CardColours.BLUE, 2);
        Card blueSkipCard = new SkipCard(CardColours.BLUE, 1);
        Card wildCard = new WildCard();
        Card wildDrawCard = new WildDrawCard(4);

        Assert.assertTrue(wildCard.canPlayOn(blueSix));
        Assert.assertTrue(wildCard.canPlayOn(blueDrawCard));
        Assert.assertTrue(wildCard.canPlayOn(blueReverseCard));
        Assert.assertTrue(wildCard.canPlayOn(blueSkipCard));

        wildCard.myColour = CardColours.BLUE;
        Assert.assertTrue(blueSix.canPlayOn(wildCard));
        Assert.assertTrue(blueDrawCard.canPlayOn(wildCard));
        Assert.assertTrue(blueReverseCard.canPlayOn(wildCard));
        Assert.assertTrue(blueSkipCard.canPlayOn(wildCard));

        Assert.assertTrue(wildDrawCard.canPlayOn(blueSix));
        Assert.assertTrue(wildDrawCard.canPlayOn(blueDrawCard));
        Assert.assertTrue(wildDrawCard.canPlayOn(blueReverseCard));
        Assert.assertTrue(wildDrawCard.canPlayOn(blueSkipCard));

        wildDrawCard.myColour = CardColours.BLUE;
        Assert.assertTrue(blueSix.canPlayOn(wildDrawCard));
        Assert.assertTrue(blueDrawCard.canPlayOn(wildDrawCard));
        Assert.assertTrue(blueReverseCard.canPlayOn(wildDrawCard));
        Assert.assertTrue(blueSkipCard.canPlayOn(wildDrawCard));
    }

    @Test
    public void canPlayOn_expectNotAllowed_blackCards() {
        initStartingDeck();
        Card blueReverseCard = new ReverseCard(CardColours.BLUE);
        Card blueDrawCard = new DrawCard(CardColours.BLUE, 2);
        Card blueSkipCard = new SkipCard(CardColours.BLUE, 1);
        Card wildCard = new WildCard();

        wildCard.myColour = CardColours.RED;
        Assert.assertFalse(blueSix.canPlayOn(wildCard));
        Assert.assertFalse(blueDrawCard.canPlayOn(wildCard));
        Assert.assertFalse(blueReverseCard.canPlayOn(wildCard));
        Assert.assertFalse(blueSkipCard.canPlayOn(wildCard));
    }

    private void initStartingDeck() {
        yellowOne = new Card(CardColours.YELLOW, CardSymbol.ONE);
        blueTwo = new Card(CardColours.BLUE, CardSymbol.TWO);
        redThree = new Card(CardColours.RED, CardSymbol.THREE);
        greenFour = new Card(CardColours.GREEN, CardSymbol.FOUR);
        yellowFive = new Card(CardColours.YELLOW, CardSymbol.FIVE);
        blueSix = new Card(CardColours.BLUE, CardSymbol.SIX);
        redSeven = new Card(CardColours.RED, CardSymbol.SEVEN);
        greenEight = new Card(CardColours.GREEN, CardSymbol.EIGHT);
    }
}
