package databaseServer.businessObjects;

public class GameObject {
    private final String UUID;
    private final String gameName;
    private final int connectedPlayers;
    private final int amountOfPlayers;
    private final boolean active;
    private final int serverport;

    public GameObject(String uuid, String gameName, int connectedPlayers, int amountOfPlayers, boolean active, int serverport) {
        UUID = uuid;
        this.gameName = gameName;
        this.connectedPlayers = connectedPlayers;
        this.amountOfPlayers = amountOfPlayers;
        this.active = active;
        this.serverport = serverport;
    }

    public String getID() {
        return UUID;
    }

    public String getName() {
        return this.gameName;
    }

    public int getConnectedPlayers() {
        return connectedPlayers;
    }

    public int getAmountOfPlayers() {
        return amountOfPlayers;
    }

    public boolean isActive() {
        return active;
    }

    public int getServerport() {
        return serverport;
    }

    public boolean getActive() {
        return this.active;
    }

    @Override
    public String toString() {
        return UUID + "\t" + gameName + "\t" + amountOfPlayers  + "\t" + connectedPlayers + "\t" + active + "\t" + serverport;
    }

    public static class Builder {
        private String UUID;
        private String gameName;
        private int connectedPlayers;
        private int amountOfPlayers;
        private boolean active;
        private int serverport;

        public Builder setUUID(String UUID) {
            this.UUID = UUID;
            return this;
        }

        public Builder setGameName(String gameName) {
            this.gameName = gameName;
            return this;
        }

        public Builder setConnectedPlayers(int connectedPlayers) {
            this.connectedPlayers = connectedPlayers;
            return this;
        }

        public Builder setAmountOfPlayers(int amountOfPlayers) {
            this.amountOfPlayers = amountOfPlayers;
            return this;
        }

        public Builder setActive(boolean active) {
            this.active = active;
            return this;
        }

        public Builder setServerport(int serverport) {
            this.serverport = serverport;
            return this;
        }

        public GameObject buildGame() {
            return new GameObject(this.UUID, this.gameName, this.connectedPlayers, this.amountOfPlayers, this.active, this.serverport);
        }
    }
}
