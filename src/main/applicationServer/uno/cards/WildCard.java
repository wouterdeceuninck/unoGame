package applicationServer.uno.cards;

import applicationServer.uno.cards.properties.CardColours;
import applicationServer.uno.cards.properties.CardSymbol;
import applicationServer.uno.UnoGame;

import java.io.Serializable;

public class WildCard extends Card implements Serializable {

	private static final long serialVersionUID = -1204492106528466734L;

	public WildCard() {
		super(CardColours.NONE, CardSymbol.WILDCARD);
	}

	@Override
	public boolean canPlayOn(Card card) {
		return true;
	}

	public void play(UnoGame game) {
	}

}
