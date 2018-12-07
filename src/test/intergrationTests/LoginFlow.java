package test.intergrationTests;

import main.client.UserController;
import main.controller.LoginController;
import main.dispatcher.Main;
import main.exceptions.InvalidInputException;
import org.junit.Before;
import org.junit.Test;

public class LoginFlow {

    @Before
    public void startDispatcher() {
        new Main().startServer();
    }

    @Test(expected = InvalidInputException.class)
    public void loginToServer_invalidInput() throws InvalidInputException {
        LoginController loginController = new LoginController();
        loginController.loginLogic("wouter", "psss");
        loginController.getUserController().getUserInfo().getToken();
    }

    @Test
    public void loginToServer_validInput() throws InvalidInputException {
        LoginController loginController = new LoginController();
        loginController.loginLogic("wouterDeceuninck", "passWord");
        System.out.println(loginController.getUserController().getUserInfo().getToken());
    }

}
