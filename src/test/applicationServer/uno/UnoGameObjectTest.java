package test.applicationServer.uno;

import main.applicationServer.uno.*;
import main.client.GameInfo;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class UnoGameObjectTest {

    @Test
    public void createGame_deckValidation() {
        UnoGame unoGame = new UnoGame(new GameInfo(UUID.randomUUID().toString(), 1, "GameName", 2));
        Assert.assertTrue(unoGame.getDeck().stream().filter(card -> card.myColour == CardColours.BLUE && card.mySymbol== CardSymbol.ONE).count() == 2L);
        Assert.assertTrue(unoGame.getDeck().stream().filter(card -> card.myColour == CardColours.GREEN && card.mySymbol == CardSymbol.REVERSECARD).count() == 2L);
    }

    @Test
    public void createGame_2botsPlayGame() {
        UnoGame newBotGame = createNewBotGame(2);
        System.out.println(newBotGame.play());
    }

    @Test
    public void createGame_3botsPlayGame() {
        UnoGame newBotGame = createNewBotGame(3);
        System.out.println(newBotGame.play());

        newBotGame.getPlayers().forEach(playerInterface -> {
            System.out.println(playerInterface.getLog() + "/n");
            Assert.assertTrue(!validatePile(playerInterface.getPile()).isPresent());
        });
    }

    @Test
    public void createGame_4botsPlayGame() {
        UnoGame newBotGame = createNewBotGame(4);
        System.out.println(newBotGame.play());

        newBotGame.getPlayers().forEach(playerInterface -> {
            System.out.println(playerInterface.getLog() + "\n");
            Assert.assertTrue(!validatePile(playerInterface.getPile()).isPresent());
        });
    }

    @Test
    public void stressTest() {
        List<String> winners = new ArrayList<>();
        for (int i = 0; i<10000; i++) {
            winners.add(createNewBotGame(4).play());
        }
        long bot1 = winners.stream().filter(winner -> winner.equals("bot1")).count();
        long bot2 = winners.stream().filter(winner -> winner.equals("bot2")).count();
        long bot3 = winners.stream().filter(winner -> winner.equals("bot3")).count();
        long bot4 = winners.stream().filter(winner -> winner.equals("bot4")).count();

        System.out.println("Bot 1 won " + bot1 + " times.");
        System.out.println("Bot 2 won " + bot2 + " times.");
        System.out.println("Bot 3 won " + bot3 + " times.");
        System.out.println("Bot 4 won " + bot4 + " times.");

        long sum = bot1 + bot2 + bot3 + bot4;
        System.out.println("Total games played: " + sum);
    }

    private Optional<String> validatePile(Deque<Card> pile) {
        Card topCard = pile.pop();
        try {
            while (true) {
                Card temp = pile.pop();
                if (!topCard.canPlayOn(temp)){
                    return Optional.of("Cannot Play card exception.\n"
                            + "TopCard: " + topCard.toString() + "\n"
                            + "CardPlayed: " + temp.toString());
                }
                topCard = temp;
            }
        } catch (NoSuchElementException e ){
            return Optional.empty();
        }
    }

    private UnoGame createNewBotGame(int amountOfPlayers) {
        String gameID = UUID.randomUUID().toString();
        UnoGame unoGame = new UnoGame(new GameInfo(gameID, 1, "GameName" + gameID, amountOfPlayers));

        for (int iter = 1; iter <= amountOfPlayers; iter++) {
            unoGame.addPlayer(new BotPlayer("bot" + iter));
        }
        return unoGame;
    }
}
