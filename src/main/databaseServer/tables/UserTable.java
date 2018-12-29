package main.databaseServer.tables;

import io.jsonwebtoken.JwtBuilder;
import main.databaseServer.businessObjects.UserObject;
import main.databaseServer.security.JwtFactory;
import main.databaseServer.security.Token;
import main.exceptions.UsernameAlreadyUsedException;

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
            String sql = "CREATE TABLE IF NOT EXISTS Users (\n"
                    + "	username    VARCHAR     PRIMARY KEY,\n" + "	password    VARCHAR     NOT NULL, \n"
                    + " token       VARCHAR     NOT NULL" + ");";
            executeStatement(sql);
    }

    private void executeStatement(String sql) {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        }catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void checkUsername(String username) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(getCheckUsernameStatement());
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                throw new UsernameAlreadyUsedException("This username is already used!");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private String getCheckUsernameStatement() {
        return "SELECT username FROM USERS WHERE USERNAME = ?";
    }

    public boolean loginUser(String username, String password) {
        return false;
    }

    public String addUser(String username, String password) {
        checkUsername(username);
        String sql = "INSERT INTO Users(username,password,token) VALUES(?,?,?)";
        String token = new Token.Builder()
                .setAlg("SHA-256")
                .setName(username)
                .setTimestamp(LocalDateTime.now().toString())
                .build()
                .toString();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, token);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return token;
    }

    public UserObject getUser(String pindakaas) {
        return null;
    }
}
