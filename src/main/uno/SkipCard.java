package main.uno;

import java.io.Serializable;

public class SkipCard extends Card implements Serializable {

	private static final long serialVersionUID = 1682533977395640829L;
	private int nSkip;

	public SkipCard(int colour, int nSkip) {
		super(colour, "SKIP");
		this.nSkip = nSkip;
		this.myScore = 20;
	}

	public int getNSkip() {
		return nSkip;
	}

	@Override
	public void play(UnoGame game) {
		// skip the nSkip player
		for (int i = 0; i < this.nSkip; i++) {
			game.goToNextPlayer();
		}
	}
}
