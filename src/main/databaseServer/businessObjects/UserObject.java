package databaseServer.businessObjects;

public class UserObject {
    private final String username;
    private final String password;
    private final String token;

    public UserObject(String username, String password, String token) {
        this.username = username;
        this.password = password;
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getToken() {
        return token;
    }

    public String toString() {
        return username + "_" + password + "_" + token;
    }
    public static class Builder {
        private String username;
        private String password;
        private String token;

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public UserObject buildUser() {
            return new UserObject(this.username, this.password, this.token);
        }
    }
}
