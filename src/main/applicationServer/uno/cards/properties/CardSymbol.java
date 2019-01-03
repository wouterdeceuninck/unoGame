package applicationServer.uno.cards.properties;

public enum CardSymbol {
    WILDCARD(50, "WILDCARD"),
    WILDDRAWCARD(50, "WILDDRAWCARD"),
    SKIPCARD(20, "SKIP"),
    DRAWCARD(20, "PLUS2"),
    REVERSECARD(20, "REVERSE"),
    NINE(9, "9"),
    EIGHT(8, "8"),
    SEVEN(7, "7"),
    SIX(6, "6"),
    FIVE(5, "5"),
    FOUR(4, "4"),
    THREE(3, "3"),
    TWO(2, "2"),
    ONE(1, "1"),
    NUL(0, "0");

    private final int score;

    private final String name;

    CardSymbol(int score, String name) {
        this.score = score;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public static CardSymbol[] getNormalSymbols() {
        return new CardSymbol[]{NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO, ONE, NUL};
    }
}
