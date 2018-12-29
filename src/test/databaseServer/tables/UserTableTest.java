package test.databaseServer.tables;

import main.databaseServer.tables.UserTable;
import main.exceptions.UsernameAlreadyUsedException;
import org.junit.Test;

public class UserTableTest {

    final String URI = "resources\\dbTables.db";

    @Test
    public void createTableTest() {
        UserTable userTable = new UserTable(URI);
    }

    @Test(expected = UsernameAlreadyUsedException.class)
    public void addUserToTable_usernameNotAvailable() {
        UserTable userTable = new UserTable(URI);
        userTable.addUser("PindaKaas", "aPassword");
        userTable.checkUsername("Pindakaas");
    }

    @Test(expected = UsernameAlreadyUsedException.class)
    public void addUserTwice_epectError() {
        UserTable userTable = new UserTable(URI);
        userTable.addUser("PindaKaas", "aPassword");
        userTable.addUser("PindaKaas", "aPassword");
    }
}
