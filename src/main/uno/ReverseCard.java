package main.uno;

import java.io.Serializable;

public class ReverseCard extends Card implements Serializable {

	private static final long serialVersionUID = 5745108777726812406L;

	public ReverseCard(int colour) {
		super(colour, "REVERSE");
		this.myScore = 20;
	}

	@Override
	public boolean canPlayOn(Card card) {
		return (card.getColour() == this.getColour() || card.getSymbol().equals(this.getSymbol()));
	}

	@Override
	public void play(UnoGame game) {
		game.reversePlayDirection();
	}
}
