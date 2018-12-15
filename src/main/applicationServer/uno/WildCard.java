package main.applicationServer.uno;

import java.io.Serializable;
import java.rmi.RemoteException;

public class WildCard extends Card implements Serializable {

	private static final long serialVersionUID = -1204492106528466734L;

	public WildCard() {
		super(CardColours.NONE, "WILDCARD");
		this.myScore = 50;
	}

	@Override
	public boolean canPlayOn(Card card) {
		return true;
	}

	public void play(UnoGame game) {
		this.myColour = game.getNextPlayer(0).askColor();
	}

}