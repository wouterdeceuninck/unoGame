package main.uno;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

public class DrawCard extends Card implements Serializable {

	private static final long serialVersionUID = 1954659785727378897L;
	private int nDraw;

	public DrawCard(int colour, int nDraw) {
		super(colour, "PLUS2");
		this.nDraw = nDraw;
		this.myScore = 20;
	}

	public int getNDraw() {
		return nDraw;
	}

	@Override
	public void play(UnoGame game) {
		try {
			List<Card> draw = game.draw(nDraw);
			game.getNextPlayer(1).getGameController().addCards(draw);
			game.getNextPlayer(1).getCards().addAll(draw);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		game.goToNextPlayer();
	}
}
