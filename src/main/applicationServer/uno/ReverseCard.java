package main.applicationServer.uno;

import java.io.Serializable;

public class ReverseCard extends Card implements Serializable {

	private static final long serialVersionUID = 5745108777726812406L;

	public ReverseCard(CardColours cardColours) {
		super(cardColours, CardSymbol.REVERSECARD);
		this.myScore = 20;
	}

	@Override
	public boolean canPlayOn(Card card) {
		return (card.getColour() == this.getColour() || card.getSymbol().equals(this.getSymbol()));
	}

	public void play(UnoGame game) {
		game.reversePlayDirection();
	}
}
