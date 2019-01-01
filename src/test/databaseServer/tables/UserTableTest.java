package databaseServer.tables;

import databaseServer.businessObjects.UserObject;
import exceptions.UnAutherizedException;
import exceptions.UserNotFoundException;
import exceptions.UsernameAlreadyUsedException;
import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

public class UserTableTest {

    //default db where PindaKaas is already registered as a user
    final String URI = "resources\\dbTables.db";

    @Test
    public void createTableTest() {
        UserTable userTable = new UserTable(URI);
    }

    @Test(expected = UsernameAlreadyUsedException.class)
    public void addUserToTable_usernameNotAvailable() {
        UserTable userTable = new UserTable(URI);
        userTable.addUser("PindaKaas", "aPassword");
    }

    @Test
    public void getUser_expectUserObject() {
        UserTable userTable = new UserTable(URI);
        UserObject userObject = userTable.getUser("PindaKaas");
        Assert.assertEquals(userObject.getUsername(), "PindaKaas");
    }

    @Test
    public void getUser_expectNull() {
        UserTable userTable = new UserTable(URI);
        UserObject userObject = userTable.getUser("graphicsmagick");
        Assert.assertTrue(userObject == null);
    }

    @Test
    public void registerUser() {
        UserTable userTable = new UserTable(URI);
        String username = UUID.randomUUID().toString();
        String token = userTable.addUser(username, "myVeryOwnPassword");
        Assert.assertTrue(!token.isEmpty());
        System.out.println(token);
    }

    @Test(expected = UnAutherizedException.class)
    public void loginUser_expectUnautherized() throws UserNotFoundException {
        UserTable userTable = new UserTable(URI);
        userTable.loginUser("PindaKaas", "wrongPassword");
    }

    @Test
    public void loginUser_expectToken() throws UserNotFoundException {
        UserTable userTable = new UserTable(URI);
        String token = userTable.loginUser("PindaKaas", "aPassword");
        Assert.assertTrue(!token.isEmpty());
        System.out.println(token);
    }
}
