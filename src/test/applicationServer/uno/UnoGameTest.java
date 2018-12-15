package test.applicationServer.uno;

import main.applicationServer.uno.BotPlayer;
import main.applicationServer.uno.CardColours;
import main.applicationServer.uno.Player;
import main.applicationServer.uno.UnoGame;
import main.client.GameInfo;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class UnoGameTest {

    @Test
    public void createGame_deckValidation() {
        UnoGame unoGame = new UnoGame(new GameInfo(1, 1, "GameName", 2));
        unoGame.newDeck();
        Assert.assertTrue(unoGame.getDeck().stream().filter(card -> card.myColour == CardColours.BLUE && card.myScore == 1).count() == 4L);
        Assert.assertTrue(unoGame.getDeck().stream().filter(card -> card.myColour == CardColours.GREEN && card.mySymbol.equals("REVERSE")).count() == 4L);
    }

    @Test
    public void createGame_2botsPlayGame() {
        List<UnoGame> games = new ArrayList<>();
        games.add(createNewBotGame(2));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        games.forEach(game -> {
                    game.getPlayers().forEach(playerInterface -> System.out.println(playerInterface.getLog() + "/n"));
                });
    }

    @Test
    public void createGame_3botsPlayGame() {
        List<UnoGame> games = new ArrayList<>();
        games.add(createNewBotGame(3));
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        games.forEach(game -> {
            game.getPlayers().forEach(playerInterface -> System.out.println(playerInterface.getLog() + "/n"));
        });
    }

    private UnoGame createNewBotGame(int amountOfPlayers) {
        UnoGame unoGame = new UnoGame(new GameInfo(1, 1, "GameName", amountOfPlayers));

        for (int iter = 1; iter <= amountOfPlayers; iter++) {
            unoGame.addPlayer(new BotPlayer("bot" + iter));
        }
        return unoGame;
    }

}
