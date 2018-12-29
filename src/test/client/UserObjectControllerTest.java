package test.client;

import main.client.UserController;
import main.interfaces.ServerInterface;
import org.junit.Assert;
import org.junit.Test;

import javax.security.auth.login.FailedLoginException;
import java.rmi.RemoteException;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserObjectControllerTest {

    ServerInterface serverInterface = mock(ServerInterface.class);
    private String value = null;

    @Test
    public void Login_serverReturnsValue() throws RemoteException {
        value = "A not so plausable token value";
        when(serverInterface.login(anyString(), anyString())).thenReturn(value);
        UserController userController = new UserController(serverInterface);
        userController.loginToServer("username", "password");
        Assert.assertEquals(userController.getUserInfo().getToken(), value);
    }

    @Test(expected = UserController.FailedToLoginException.class)
    public void Login_serverReturnsNullThrowsException() throws RemoteException {
        when(serverInterface.login(anyString(), anyString())).thenReturn(value);
        UserController userController = new UserController(serverInterface);
        userController.loginToServer("username", "password");
    }

    @Test
    public void Register_serverReturnsValue() throws RemoteException, FailedLoginException {
        value = "A not so plausable token value";
        when(serverInterface.register(anyString(), anyString())).thenReturn(value);
        UserController userController = new UserController(serverInterface);
        userController.registerToServer("username", "password");
        Assert.assertEquals(userController.getUserInfo().getToken(), value);
    }

    @Test(expected = UserController.FailedToLoginException.class)
    public void Register_serverReturnsNullThrowsException() throws RemoteException, FailedLoginException {
        when(serverInterface.register(anyString(), anyString())).thenReturn(value);
        UserController userController = new UserController(serverInterface);
        userController.registerToServer("username", "password");
    }

}