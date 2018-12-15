package main.applicationServer.uno;

public enum CardColours {
    YELLOW("yellow"),
    BLUE("blue"),
    RED("red"),
    GREEN("green"),
    NONE("");

    public final String colour;

    CardColours (final String colour) {
        this.colour = colour;
    }

    static CardColours[] getColours() {
        return new CardColours[]{YELLOW, BLUE, RED, GREEN};
    }
}
