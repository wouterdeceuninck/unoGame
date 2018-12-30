package databaseServer.tables;

import databaseServer.businessObjects.GameObject;
import exceptions.GameFullException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GameTable {
    private Connection connection;

    public GameTable(String uri) {
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + uri);
            createGameTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String addGame(GameObject gameObject){
        String ID = UUID.randomUUID().toString();
        while (!checkID(ID)) ID = UUID.randomUUID().toString();

        try (PreparedStatement preparedStatement = connection.prepareStatement(getInsertGameStatement())) {
            preparedStatement.setString(1, ID);
            preparedStatement.setString(2, gameObject.getName());
            preparedStatement.setInt(3, gameObject.getAmountOfPlayers());
            preparedStatement.setInt(4, gameObject.getConnectedPlayers());
            preparedStatement.setBoolean(5, gameObject.isActive());
            preparedStatement.setInt(6, gameObject.getServerport());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return ID;
    }

    public GameObject getGame(String ID) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(getSelectGameStatement())) {
            preparedStatement.setString(1, ID);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) return null;
            return createGameObject(resultSet);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<GameObject> getActiveGames() {
        List<GameObject> results = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(getAllGamesStatement())) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                results.add(createGameObject(resultSet));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return results;
    }

    public void setInactive(String game_id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(getSetInactiveStatement())) {
            preparedStatement.setString(1, game_id);
            if (preparedStatement.executeUpdate() != 1) throw new RuntimeException();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addUserToGame(String game_id) {
        if (isGameFull(game_id)) throw new GameFullException("The game you selected is already full!");
        try (PreparedStatement preparedStatement = connection.prepareStatement(getAddUserToGameStatement())) {
            preparedStatement.setString(1, game_id);
            if (preparedStatement.executeUpdate() != 1) throw new RuntimeException();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserFromGame(String game_id) {
        if(isGameEmpty(game_id)) throw new RuntimeException("Something has gone wrong in the server (less than 0 players)");
        try (PreparedStatement preparedStatement = connection.prepareStatement(getRemoveUserFromGameStatement())) {
            preparedStatement.setString(1, game_id);
            if (preparedStatement.executeUpdate() != 1) throw new RuntimeException();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean isGameFull(String game_id) {
        GameObject game = getGame(game_id);
        return game.getAmountOfPlayers() == game.getConnectedPlayers();
    }

    private boolean isGameEmpty(String game_id) {
        return getGame(game_id).getAmountOfPlayers() == 0;
    }

    private void createGameTable() {
        String sql = getCreateTableStatement();
        executeStatement(sql);
    }

    private void executeStatement(String sql) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private boolean checkID(String id) {
        GameObject gameObject = getGame(id);
        if (gameObject == null) return true;
        return false;
    }

    private GameObject createGameObject(ResultSet resultSet) throws SQLException {
        return new GameObject.Builder()
                .setUUID(resultSet.getString("game_id"))
                .setGameName(resultSet.getString("game_name"))
                .setAmountOfPlayers(resultSet.getInt("amount_of_players"))
                .setConnectedPlayers(resultSet.getInt("connected_players"))
                .setActive(resultSet.getBoolean("active"))
                .setServerport(resultSet.getInt("serverport"))
                .buildGame();
    }

    private String getAllGamesStatement() {
        return "SELECT * FROM Game WHERE active";
    }

    private String getInsertGameStatement() {
        return "INSERT INTO Game(game_id, game_name, amount_of_players, connected_players, active ,serverport) VALUES(?,?,?,?,?,?)";
    }

    private String getSelectGameStatement() {
        return "SELECT * FROM Game WHERE game_id = ?";
    }

    private String getCreateTableStatement() {
        return  "CREATE TABLE IF NOT EXISTS Game (\n"
                + "	game_id     	VARCHAR     PRIMARY KEY,\n"
                + "	game_name       VARCHAR     NOT NULL,\n" + " amount_of_players 	INTEGER     NOT NULL,\n"
                + " connected_players  	BOOLEAN     NOT NULL, \n" + " active 	BOOLEAN     NOT NULL,\n"
                + " serverport		INTEGER		NOT NULL \n" + ");";
    }

    private String getSetInactiveStatement() {
        return "UPDATE Game SET active = 0 WHERE game_id = ?";
    }

    private String getAddUserToGameStatement() {
        return "UPDATE Game SET connected_players = connected_players + 1 WHERE game_id = ?";
    }

    private String getRemoveUserFromGameStatement() {
        return "UPDATE Game SET connected_players = connected_players - 1 WHERE game_id = ?";
    }
}
