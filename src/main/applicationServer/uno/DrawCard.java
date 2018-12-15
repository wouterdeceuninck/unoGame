package main.applicationServer.uno;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

public class DrawCard extends Card implements Serializable {

	private static final long serialVersionUID = 1954659785727378897L;
	private int nDraw;

	public DrawCard(CardColours colour, int nDraw) {
		super(colour, "PLUS2");
		this.nDraw = nDraw;
		this.myScore = 20;
	}

	public int getNDraw() {
		return nDraw;
	}

	public void play(UnoGame game) {
		List<Card> draw = game.draw(nDraw);
		game.getNextPlayer(1).addCards(draw);
		game.getNextPlayer(1).getCards().addAll(draw);
		game.goToNextPlayer();
	}
}
