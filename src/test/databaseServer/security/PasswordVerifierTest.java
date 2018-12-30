package databaseServer.security;

import databaseServer.businessObjects.UserObject;
import org.junit.Assert;
import org.junit.Test;

public class PasswordVerifierTest {

    @Test
    public void generatePassword() {
        String password = "aPassword";
        System.out.println(PasswordVerifier.createPassword(password));
        Assert.assertTrue(PasswordVerifier.verifyPassword(password, createUser(password)));
    }

    private UserObject createUser(String password) {
        return new UserObject.Builder().setPassword(PasswordVerifier.createPassword(password)).buildUser();
    }
}
