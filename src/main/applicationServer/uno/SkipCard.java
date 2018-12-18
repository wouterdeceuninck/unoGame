package main.applicationServer.uno;

import java.io.Serializable;

public class SkipCard extends Card implements Serializable {

	private static final long serialVersionUID = 1682533977395640829L;
	private int nSkip;

	public SkipCard(CardColours colour, int nSkip) {
		super(colour, CardSymbol.SKIPCARD);
		this.nSkip = nSkip;
		this.myScore = 20;
	}

	public int getNSkip() {
		return nSkip;
	}

	public void play(UnoGame game) {
		// skip the nSkip player
		for (int i = 0; i < this.nSkip; i++) {
			game.goToNextPlayer();
		}
	}
}
