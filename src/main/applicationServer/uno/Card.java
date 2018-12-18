package main.applicationServer.uno;

import java.io.Serializable;

import javafx.scene.image.Image;

public class Card implements Serializable {

	private static final long serialVersionUID = -467804728581745981L;

	public CardColours myColour;
	public CardSymbol mySymbol;
	public String cardName;
	public int myScore;

	public Card(CardColours colour, CardSymbol symbol) {
		myColour = colour;
		mySymbol = symbol;
		cardName = mySymbol + "_" + colour + ".png";
	}

	public Card(Card selectedCard) {
		this.cardName = selectedCard.cardName;
		this.myColour = selectedCard.myColour;
		this.mySymbol = selectedCard.mySymbol;
	}

	public String getColour() {
		return myColour.colour;
	}

	public CardSymbol getSymbol() {
		return mySymbol;
	}

	public boolean canPlayOn(Card card) {
		boolean sameColorOrSymbol = card.myColour == myColour || card.mySymbol.equals(mySymbol);
		boolean isWildCard = this.mySymbol.equals("WILDCARD") || this.mySymbol.equals("WILDDRAWCARD");
		return isWildCard || sameColorOrSymbol;
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
