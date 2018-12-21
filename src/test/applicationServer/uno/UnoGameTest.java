package test.applicationServer.uno;

import main.applicationServer.uno.*;
import main.client.GameInfo;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class UnoGameTest {

    @Test
    public void createGame_deckValidation() {
        UnoGame unoGame = new UnoGame(new GameInfo(UUID.randomUUID().toString(), 1, "GameName", 2));
        Assert.assertTrue(unoGame.getDeck().stream().filter(card -> card.myColour == CardColours.BLUE && card.mySymbol== CardSymbol.ONE).count() == 2L);
        Assert.assertTrue(unoGame.getDeck().stream().filter(card -> card.myColour == CardColours.GREEN && card.mySymbol == CardSymbol.REVERSECARD).count() == 4L);

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
                    game.getPlayers().forEach(playerInterface -> System.out.println(playerInterface.getLog() + "\n"));
                });
    }

    @Test
    public void createGame_3botsPlayGame() {
        List<UnoGame> games = new ArrayList<>();
        games.add(createNewBotGame(3));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        games.forEach(game -> {
            game.getPlayers().forEach(playerInterface -> {
                System.out.println(playerInterface.getLog() + "/n");
                Assert.assertTrue(!validatePile(playerInterface.getPile()).isPresent());
            });

        });
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
            }
        } catch (NoSuchElementException e ){
            return Optional.empty();
        }
    }

    @Test
    public void createGame_4botsPlayGame() {
        List<UnoGame> games = new ArrayList<>();
        games.add(createNewBotGame(4));
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        games.forEach(game -> {
            game.getPlayers().forEach(playerInterface -> System.out.println(playerInterface.getLog() + "/n"));
        });
    }

    private UnoGame createNewBotGame(int amountOfPlayers) {
        String gameID = UUID.randomUUID().toString();
        UnoGame unoGame = new UnoGame(new GameInfo(gameID, 1, "GameName" + gameID, amountOfPlayers));

        for (int iter = 1; iter <= amountOfPlayers; iter++) {
            unoGame.addPlayer(new BotPlayer("bot" + iter));
        }
        return unoGame;
    }

    @Test
    public void testExecutorService() {
        ExecutorService threadPool = Executors.newFixedThreadPool(200);
        List<UnoGame> games = new ArrayList<>();
        for (int i = 0; i< 5000; i++) {
            threadPool.submit(() -> {
                games.add(createNewBotGame(2));
                games.add(createNewBotGame(3));
                games.add(createNewBotGame(4));
            });
        }
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(150, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
        games.forEach(game -> {
            AtomicInteger iter = new AtomicInteger(0);
            game.getPlayers().forEach(playerInterface -> {
                Optional<String> s = validatePile(playerInterface.getPile());
                System.out.println(s.orElse(iter.getAndIncrement() + "\t\t\t\t\tNo error"));
                Assert.assertTrue(!s.isPresent());
            });
        });
    }
}
