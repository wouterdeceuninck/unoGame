package test.controller;

import main.client.UserController;
import main.controller.LoginController;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoginControllerTest {

    LoginController loginController = new LoginController();

    @Test
    public void isValidRegisterInput_expectValid() {
        Assert.assertTrue(loginController.isValidRegisterInput("username", "password", "password"));
    }

    @Test
    public void isValidRegisterInput_expectInvalid_passwordNotTheSame() {
        Assert.assertFalse(loginController.isValidRegisterInput("username", "password", "password2"));
    }

    @Test
    public void isValidRegisterInput_expectInvalid_passwordTooShort() {
        Assert.assertFalse(loginController.isValidRegisterInput("username", "pass", "pass"));
    }

    @Test
    public void isValidLoginInput_expectvalid() {
        Assert.assertTrue(loginController.isValidLoginInput("username", "password"));
    }

    @Test
    public void isValidLoginInput_expectInvalid_usernameTooShort() {
        Assert.assertFalse(loginController.isValidLoginInput("user", "password"));
    }

    @Test
    public void isValidLoginInput_expectInvalid_passwordTooShort() {
        Assert.assertFalse(loginController.isValidLoginInput("username", "pass"));
    }
}