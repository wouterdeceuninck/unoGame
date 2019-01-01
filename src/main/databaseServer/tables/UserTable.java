package databaseServer.tables;

import databaseServer.businessObjects.UserObject;
import databaseServer.security.PasswordVerifier;
import databaseServer.security.Token;
import exceptions.UnAutherizedException;
import exceptions.UserNotFoundException;
import exceptions.UsernameAlreadyUsedException;

import java.sql.*;
import java.time.LocalDateTime;

public class UserTable {
    private Connection connection;

    public UserTable(String uri) {
        try {
            this.connection = DriverManager.getConnection("jdbc:sqlite:" + uri);
            createUserTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void createUserTable() {
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

    private void checkUsername(String username) throws UsernameAlreadyUsedException {
        if (getUser(username) != null) {
            throw new UsernameAlreadyUsedException("This username is already used!");
        }
    }

    public UserObject getUser(String username){
        try (PreparedStatement preparedStatement = connection.prepareStatement(getSelectUserStatement())) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) return null;
            return new UserObject.Builder()
                    .setUsername(resultSet.getString("username"))
                    .setPassword(resultSet.getString("password"))
                    .setToken(resultSet.getString("token"))
                    .buildUser();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String loginUser(String username, String password) throws UnAutherizedException, UserNotFoundException {
        UserObject user = getUser(username);
        String token = createToken(username);
        if (user != null) {
            if (!PasswordVerifier.verifyPassword(password, user)) throw new UnAutherizedException("The username or password given are not correct");
            updateUser(username, token);
        } else throw new UserNotFoundException("The username or password given are not correct");
        return token;
    }

    private void updateUser(String username, String token){
        try (PreparedStatement preparedStatement = connection.prepareStatement(getUpdateUserStatement())) {
            preparedStatement.setString(1, token);
            preparedStatement.setString(2, username);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String addUser(String username, String hashedAndSalt) throws UsernameAlreadyUsedException{
        checkUsername(username);
        String token = createToken(username);
        insertUser(username, hashedAndSalt, token);
        return token;
    }

    private void insertUser(String username, String hashedPassword, String token) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(getInsertUserStatement())) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashedPassword);
            preparedStatement.setString(3, token);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void duplicateAddUser(String username, String password, String token) {
        insertUser(username, password, token);
    }

    public void duplicateLoginUser(String username, String token) {
        updateUser(username, token);
    }

    private String createToken(String username) {
        return new Token.Builder()
                    .setAlg("SHA-256")
                    .setName(username)
                    .setTimestamp(LocalDateTime.now().toString())
                    .build()
                    .toString();
    }

    private String getCreateTableStatement() {
        return "CREATE TABLE IF NOT EXISTS Users (\n"
                + "	username    VARCHAR     PRIMARY KEY,\n" + "	password    VARCHAR     NOT NULL, \n"
                + " token       VARCHAR     NOT NULL" + ");";
    }

    private String getSelectUserStatement() {
        return "SELECT * FROM USERS WHERE USERNAME = ?";
    }

    private String getInsertUserStatement() {
        return "INSERT INTO Users(username,password,token) VALUES(?,?,?)";
    }

    public String getUpdateUserStatement() {
        return "UPDATE USERS SET token = ? WHERE USERNAME = ?";
    }
}
