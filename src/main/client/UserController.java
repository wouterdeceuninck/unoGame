package main.client;

import main.exceptions.UsernameAlreadyUsedException;
import main.interfaces.ServerInterface;

import javax.security.auth.login.FailedLoginException;
import java.rmi.RemoteException;
import java.util.Optional;

public class UserController {
    private UserInfo userInfo;
    private GameInfo gameInfo;
    private ServerInterface server;

    public UserController(ServerInterface serverInterface) {
        this.server = serverInterface;
    }

    public void loginToServer(final String username, final String password) throws RemoteException, FailedToLoginException {
        this.userInfo = new UserInfo.InnerBuilder().setName(username)
                .setToken(Optional.ofNullable(server.login(username, password)).orElseThrow(() ->
                        new FailedToLoginException("Failed to login to the account, please try again!")
                )).buildUserInfo();
    }

    public void registerToServer(final String username, final String password) throws RemoteException, FailedLoginException, UsernameAlreadyUsedException {
        this.userInfo = new UserInfo.InnerBuilder().setName(username)
                .setToken(Optional.ofNullable(server.register(username, password)).orElseThrow(() ->
                        new FailedToLoginException("Failed to login to the account, please try again!")
                )).buildUserInfo();
    }

    public void setGameInfo(GameInfo gameInfo) {
        this.gameInfo = gameInfo;
    }

    public void sendGameMsg(UserController userController, String message) throws RemoteException {
        server.sendGameMsg(message, userController.gameInfo.getGameID(), userController.getUserInfo().getUsername());
    }

    public class FailedToLoginException extends RuntimeException {
        public FailedToLoginException(final String message) {
            super(message);
        }
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public GameInfo getGameInfo() {
        return gameInfo;
    }

    public ServerInterface getServer() {
        return server;
    }
}
