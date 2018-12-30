package applicationServer.uno.cards;

import applicationServer.uno.cards.properties.CardColours;
import applicationServer.uno.cards.properties.CardSymbol;
import applicationServer.uno.UnoGame;

import java.io.Serializable;
import java.util.List;

public class DrawCard extends Card implements Serializable {

	private static final long serialVersionUID = 1954659785727378897L;
	private int nDraw;

	public DrawCard(CardColours colour, int nDraw) {
		super(colour, CardSymbol.DRAWCARD);
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
