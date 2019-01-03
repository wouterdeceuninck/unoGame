package client.businessObjects;

import java.io.Serializable;
import java.util.Optional;

public class UserInfo implements Serializable {
    private String name;
    private String token;

    public UserInfo(String name, String token) {
        this.name = name;
        this.token = token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return name;
    }

    public String getToken() {
        return Optional.ofNullable(token).orElseThrow(() -> new NotLoggedInException("You are not logged in, please log in again"));
    }

    private class NotLoggedInException extends RuntimeException {
        private NotLoggedInException(final String message) {
            super(message);
        }
    }

    public static class InnerBuilder {
        private String name;
        private String token;

        public InnerBuilder() {
        }

        public InnerBuilder setName(String name) {
            this.name = name;
            return this;
        }

        public InnerBuilder setToken(String token) {
            this.token = token;
            return this;
        }

        public UserInfo buildUserInfo() {
            return new UserInfo(this.name, this.token);
        }
    }
}
