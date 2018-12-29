package main.databaseServer.businessObjects;

public class UserObject {
    private final String username;
    private final String password;
    private final String token;

    public UserObject(String username, String password, String token) {
        this.username = username;
        this.password = password;
        this.token = token;
    }

    public static class Builder {
        private String username;
        private String password;
        private String token;

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public UserObject buildUser() {
            return new UserObject(this.username, this.password, this.token);
        }
    }
}
