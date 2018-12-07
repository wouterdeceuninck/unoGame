package main.uno;

import java.io.Serializable;
import java.rmi.RemoteException;

public class WildCard extends Card implements Serializable {

	private static final long serialVersionUID = -1204492106528466734L;

	public WildCard() {
		super(COLOUR_NONE, "WILDCARD");
		this.myScore = 50;
	}

	@Override
	public boolean canPlayOn(Card card) {
		return true;
	}

	@Override
	public void play(UnoGame game) {
		try {
			this.myColour = game.getNextPlayer(0).getGameController().askColor();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

}
