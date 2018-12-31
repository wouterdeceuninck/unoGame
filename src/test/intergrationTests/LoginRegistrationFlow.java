package intergrationTests;

import client.controller.LoginController;
import dispatcher.Main;
import exceptions.InvalidInputException;
import org.junit.Test;

import javax.security.auth.login.FailedLoginException;

public class LoginRegistrationFlow {

    private void startDispatcher() {
        new Main().startServer();
    }

    @Test(expected = InvalidInputException.class)
    public void loginToServer_invalidInput() throws InvalidInputException {
        LoginController loginController = new LoginController();
        loginController.loginLogic("wouter", "psss");
        loginController.getUserController().getUserInfo().getToken();
    }

    @Test(expected = InvalidInputException.class)
    public void loginToServer_invalidInput_shortUsername() throws InvalidInputException {
        LoginController loginController = new LoginController();
        loginController.loginLogic("wout", "password123");
        loginController.getUserController().getUserInfo().getToken();
    }

    @Test(expected = InvalidInputException.class)
    public void loginToServer_invalidInput_shortPassword() throws InvalidInputException {
        LoginController loginController = new LoginController();
        loginController.loginLogic("wouterDeceuninck", "psss");
        loginController.getUserController().getUserInfo().getToken();
    }


    @Test(expected = InvalidInputException.class)
    public void registerToServer_invalidInput() throws FailedLoginException, InvalidInputException {
        LoginController loginController = new LoginController();
        loginController.registerLogic("wouterDeceuninck", "passWord ", "passWord");
        System.out.println(loginController.getUserController().getUserInfo().getToken());
    }

    @Test
    public void registerToServer_validInput() throws FailedLoginException, InvalidInputException {
        startDispatcher();
        LoginController loginController = new LoginController();
        loginController.registerLogic("wouterDeceuninck", "passWord", "passWord");
        System.out.println(loginController.getUserController().getUserInfo().getToken());

        loginController.loginLogic("wouterDeceuninck", "passWord");
        System.out.println(loginController.getUserController().getUserInfo().getToken());
    }

}
