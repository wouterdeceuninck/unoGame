package databaseServer.tables;

import databaseServer.businessObjects.GameObject;
import exceptions.GameFullException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class GameTableTest {
    final String URI = "resources\\dbTables.db";

    @Test
    public void createGameTable() {
        GameTable gameTable = new GameTable(URI);
    }

    @Test
    public void addGame() {
        GameTable gameTable = new GameTable(URI);
        System.out.println(gameTable.addGame(createGame()));
    }

    @Test
    public void getAllGames() {
        GameTable gameTable = new GameTable(URI);
        List<GameObject> activeGames = gameTable.getActiveGames();
        activeGames.forEach(gameObject -> System.out.println(gameObject.toString()));
    }

    @Test
    public void getGameByID() {
        GameTable gameTable = new GameTable(URI);
        GameObject originalGameObject = createGame();

        String game_id = gameTable.addGame(originalGameObject);
        GameObject requestedGameObject = gameTable.getGame(game_id);

        Assert.assertEquals(originalGameObject.getName(), requestedGameObject.getName());
        Assert.assertEquals(game_id, requestedGameObject.getID());
        Assert.assertEquals(originalGameObject.getAmountOfPlayers(), requestedGameObject.getAmountOfPlayers());
        Assert.assertEquals(originalGameObject.getConnectedPlayers(), requestedGameObject.getConnectedPlayers());
        Assert.assertEquals(originalGameObject.getServerport(), requestedGameObject.getServerport());
        Assert.assertEquals(originalGameObject.getActive(), requestedGameObject.getActive());
    }

    @Test
    public void setInactive() {
        GameTable gameTable = new GameTable(URI);
        String game_id = gameTable.addGame(createGame());

        gameTable.setInactive(game_id);

        Assert.assertFalse(gameTable.getGame(game_id).getActive());
    }

    @Test
    public void addUserToGame() throws GameFullException {
        GameTable gameTable = new GameTable(URI);
        GameObject game = createGame();
        String game_id = gameTable.addGame(game);

        gameTable.addUserToGame(game_id);
        Assert.assertEquals(gameTable.getGame(game_id).getConnectedPlayers(), game.getConnectedPlayers() + 1);

        gameTable.addUserToGame(game_id);
        Assert.assertEquals(gameTable.getGame(game_id).getConnectedPlayers(), game.getConnectedPlayers() + 2);

        gameTable.addUserToGame(game_id);
        Assert.assertEquals(gameTable.getGame(game_id).getConnectedPlayers(), game.getConnectedPlayers() + 3);
    }

    @Test
    public void removeUserFromGame() {
        GameTable gameTable = new GameTable(URI);
        GameObject game = new GameObject.Builder()
                .setGameName("myNewGame")
                .setAmountOfPlayers(3)
                .setConnectedPlayers(3)
                .setActive(true)
                .setServerport(1300)
                .buildGame();
        String game_id = gameTable.addGame(game);

        gameTable.removeUserFromGame(game_id);
        Assert.assertEquals(gameTable.getGame(game_id).getConnectedPlayers(), game.getConnectedPlayers() - 1);

        gameTable.removeUserFromGame(game_id);
        Assert.assertEquals(gameTable.getGame(game_id).getConnectedPlayers(), game.getConnectedPlayers() - 2);

        gameTable.removeUserFromGame(game_id);
        Assert.assertEquals(gameTable.getGame(game_id).getConnectedPlayers(), game.getConnectedPlayers() - 3);
    }

    private GameObject createGame() {
        return new GameObject.Builder()
                .setGameName("myNewGame")
                .setAmountOfPlayers(3)
                .setConnectedPlayers(0)
                .setActive(true)
                .setServerport(1300)
                .buildGame();
    }
}
