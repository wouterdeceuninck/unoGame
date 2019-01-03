package applicationServer.uno.cards;

import applicationServer.uno.cards.properties.CardColours;
import applicationServer.uno.cards.properties.CardSymbol;
import applicationServer.uno.UnoGame;

import java.io.Serializable;
import java.util.List;

public class WildDrawCard extends Card implements Serializable {

	private static final long serialVersionUID = 7436623692293175762L;
	private int nDraw;

	public WildDrawCard(int nDraw) {
		super(CardColours.NONE, CardSymbol.WILDDRAWCARD);
		this.nDraw = nDraw;
		this.myScore = 50;
	}

	@Override
	public boolean canPlayOn(Card card) {
		return true;
	}

	public void play(UnoGame game) {
		List<Card> draw = game.draw(nDraw);
		game.getNextPlayer(1).addCards(draw);
		game.getNextPlayer(1).getCards().addAll(draw);
		game.sendMsg("Chosen colour is " + myColour);
		game.goToNextPlayer();
	}
}
