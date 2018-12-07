package main.uno;

import java.io.Serializable;

import javafx.scene.image.Image;

public class Card implements Serializable {

	private static final long serialVersionUID = -467804728581745981L;
	public static final int COLOUR_NONE = 0;
	public static final int COLOUR_GREEN = 1;
	public static final int COLOUR_BLUE = 2;
	public static final int COLOUR_RED = 3;
	public static final int COLOUR_YELLOW = 4;
	public static final String[] COLOUR_NAMES = { "", "GREEN", "BLUE", "RED", "YELLOW" };

	public int myColour;
	public String mySymbol;
	public String cardName;
	public int myScore;

	public Card(int colour, String symbol) {
		myColour = colour;
		mySymbol = symbol;
		cardName = mySymbol + "_" + COLOUR_NAMES[colour] + ".png";
	}

	public Card(int colour, int number) {
		myColour = colour;
		mySymbol = String.valueOf(number);
		cardName = mySymbol + "_" + COLOUR_NAMES[colour] + ".png";
		myScore = number;
	}

	public Card(Card selectedCard) {
		this.cardName = selectedCard.cardName;
		this.myColour = selectedCard.myColour;
		this.mySymbol = selectedCard.mySymbol;
	}

	public int getColour() {
		return myColour;
	}

	public void setColour(int colour) {
		myColour = colour;
	}

	public String getSymbol() {
		return mySymbol;
	}

	public void setSymbol(int symbol) {
		this.mySymbol = String.valueOf(symbol);
	}

	public boolean canPlayOn(Card card) {
		return (card.mySymbol.equals(mySymbol) || card.myColour == myColour);
	}

	@Override
	public String toString() {
		String result;

		result = COLOUR_NAMES[myColour];
		if (!result.isEmpty()) {
			result += " ";
		}
		result += mySymbol;
		return result;
	}

	public Image getImage() {
		return new Image(Card.class.getResourceAsStream("SEVEN_YELLOW.png"));
	}
}
