package main.databaseServer.businessObjects;

public class GameObject {
    private final String UUID;
    private final String gameName;
    private final int connectedPlayers;
    private final int amountOfPlayers;
    private final boolean active;

    public GameObject(String uuid, String gameName, int connectedPlayers, int amountOfPlayers, boolean active) {
        UUID = uuid;
        this.gameName = gameName;
        this.connectedPlayers = connectedPlayers;
        this.amountOfPlayers = amountOfPlayers;
        this.active = active;
    }

    public static class Builder {
        private String UUID;
        private String gameName;
        private int connectedPlayers;
        private int amountOfPlayers;
        private boolean active;

        public void setUUID(String UUID) {
            this.UUID = UUID;
        }

        public void setGameName(String gameName) {
            this.gameName = gameName;
        }

        public void setConnectedPlayers(int connectedPlayers) {
            this.connectedPlayers = connectedPlayers;
        }

        public void setAmountOfPlayers(int amountOfPlayers) {
            this.amountOfPlayers = amountOfPlayers;
        }

        public void setActive(boolean active) {
            this.active = active;
        }

        public GameObject buildUser() {
            return new GameObject(this.UUID, this.gameName, this.connectedPlayers, this.amountOfPlayers, this.active);
        }
    }
}
