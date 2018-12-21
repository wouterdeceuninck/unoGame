package main.applicationServer.uno;

public enum CardSymbol {
    WILDCARD(50),
    WILDDRAWCARD(50),
    SKIPCARD(20),
    DRAWCARD(20),
    REVERSECARD(20),
    NINE(9),
    EIGHT(8),
    SEVEN(7),
    SIX(6),
    FIVE(5),
    FOUR(4),
    THREE(3),
    TWO(2),
    ONE(1),
    NULL(0);

    private final int score;

    CardSymbol (int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    static CardSymbol[] getNormalSymbols() {
        return new CardSymbol[]{NINE, EIGHT, SEVEN, SIX, FIVE, FOUR, THREE, TWO, ONE, NULL};
    }
}
