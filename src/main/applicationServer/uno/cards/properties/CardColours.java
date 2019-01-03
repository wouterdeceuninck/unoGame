package applicationServer.uno.cards.properties;

public enum CardColours {
    YELLOW("YELLOW"),
    BLUE("BLUE"),
    RED("RED"),
    GREEN("GREEN"),
    NONE("");

    public final String colour;

    CardColours (final String colour) {
        this.colour = colour;
    }

    public static CardColours[] getColours() {
        return new CardColours[]{YELLOW, BLUE, RED, GREEN};
    }
}
