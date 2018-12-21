package main.client.controller;

public enum OpponentGUISide {
    NORD(100, 180),
    EAST(50,90),
    WEST(50,270);

    private int fitHeightSize;
    private int rotationDegrees;

    OpponentGUISide(int fitHeightSize, int rotationDegrees) {
        this.fitHeightSize = fitHeightSize;
        this.rotationDegrees = rotationDegrees;
    }

    public int getFitHeightSize() {
        return fitHeightSize;
    }

    public int getRotationDegrees() {
        return rotationDegrees;
    }
}
