package main.applicationServer.uno;

import java.io.Serializable;

import javafx.scene.image.Image;

public class Card implements Serializable {

	private static final long serialVersionUID = -467804728581745981L;

	public CardColours myColour;
	public String mySymbol;
	public String cardName;
	public int myScore;

	public Card(CardColours colour, String symbol) {
		myColour = colour;
		mySymbol = symbol;
		cardName = mySymbol + "_" + colour + ".png";
	}

	public Card(CardColours colour, int number) {
		myColour = colour;
		mySymbol = String.valueOf(number);
		cardName = mySymbol + "_" + colour + ".png";
		myScore = number;
	}

	public Card(Card selectedCard) {
		this.cardName = selectedCard.cardName;
		this.myColour = selectedCard.myColour;
		this.mySymbol = selectedCard.mySymbol;
	}

	public String getColour() {
		return myColour.colour;
	}

	public String getSymbol() {
		return mySymbol;
	}

	public boolean canPlayOn(Card card) {
		return (card.myColour == myColour || card.mySymbol.equals(mySymbol));
	}

	@Override
	public String toString() {
		StringBuilder cardString = new StringBuilder(myColour.colour);
		if (!myColour.colour.isEmpty()) {
			cardString.append(" ");
		}
		cardString.append(mySymbol);
		return cardString.toString();
	}

	public Image getImage() {
		return new Image(Card.class.getResourceAsStream("SEVEN_YELLOW.png"));
	}
}
