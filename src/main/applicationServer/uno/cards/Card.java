package applicationServer.uno.cards;

import java.io.Serializable;

import applicationServer.uno.UnoGame;
import applicationServer.uno.cards.properties.CardColours;
import applicationServer.uno.cards.properties.CardSymbol;
import javafx.scene.image.Image;

public class Card implements Serializable {

	private static final long serialVersionUID = -467804728581745981L;

	public CardColours myColour;
	public CardSymbol mySymbol;
	public String cardName;
	public int myScore;

	public Card(CardColours myColour, CardSymbol symbol) {
		this.myColour = myColour;
		this.mySymbol = symbol;
		cardName = mySymbol.getName() + "_" + myColour.colour + ".png";
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
		boolean sameColorOrSymbol = card.myColour == myColour || card.mySymbol == mySymbol;
		boolean isWildCard = this.mySymbol == CardSymbol.WILDCARD || this.mySymbol == CardSymbol.WILDDRAWCARD;
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

	@Override
    public boolean equals(Object object) {
        Card card = (Card) object;
        boolean sameSymbol = card.mySymbol == this.mySymbol;
        boolean sameColor= card.myColour == this.myColour;
		boolean isWildCard = this.mySymbol == CardSymbol.WILDCARD && card.mySymbol == CardSymbol.WILDCARD;
		boolean isWildDrawCard = this.mySymbol == CardSymbol.WILDDRAWCARD && card.mySymbol == CardSymbol.WILDDRAWCARD;

        return (sameColor && sameSymbol) || isWildCard || isWildDrawCard;
    }

	public Image getImage() {
		return new Image(Card.class.getResourceAsStream("SEVEN_YELLOW.png"));
	}

	public void play(UnoGame unoGame) {

	}
}
