package applicationServer.uno.player;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import applicationServer.uno.cards.properties.CardColours;
import applicationServer.uno.cards.Card;
import exceptions.PlayerDisconnected;
import interfaces.gameControllerInterface;

public class Player implements PlayerInterface {
	private String name;
	private gameControllerInterface gameController;
	private List<Card> cards;
	private int score;
	private boolean ready;

	public Player(String name, gameControllerInterface gameController) {
		this.name = name;
		this.score = 0;
		this.gameController = gameController;
		cards = new ArrayList<>();
	}

	public void setGameControllerInterface(gameControllerInterface gameController) {
		this.gameController = gameController;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public gameControllerInterface getGameController() {
		return gameController;
	}

	public List<Card> getCards() {
		return cards;
	}

	public void setScore() {
		for (Card card : cards) {
			this.score += card.myScore;
		}
		ready = false;
	}

	public void setReady(boolean bool) {
		this.ready = bool;
	}

	@Override
	public void sendMessage(String message) {
		try {
			this.gameController.setMsg(message);
		} catch (RemoteException e) {
			throw new PlayerDisconnected(this.name + " has left the game!");
		}
	}

	public boolean isReady() {
		return ready;
	}

	@Override
	public void setPlayerNotReady() {
		this.ready = false;
	}

	@Override
	public CardColours askColor() {
		try {
			return this.gameController.askColor();
		} catch (RemoteException e) {
			throw new PlayerDisconnected(this.name + " has left the game!");
		}
	}

	@Override
	public String getLog() {
		return "";
	}

	@Override
	public Deque<Card> getPile() {
		return null;
	}

	@Override
	public void setCardAmountPlayer(String name, int size) {
		try {
			this.gameController.setCardAmountPlayer(name, size);
		} catch (RemoteException e) {
			throw new PlayerDisconnected(this.name + " has left the game!");
		}
	}

	@Override
	public void addCards(List<Card> draw) {
		try {
			this.gameController.addCards(draw);
		} catch (RemoteException e) {
			throw new PlayerDisconnected(this.name + " has left the game!");
		}
	}

	@Override
	public void addPile(Card card) {
		try {
			this.gameController.addPile(card);
		} catch (RemoteException e) {
			throw new PlayerDisconnected(this.name + " has left the game!");
		}
	}

	@Override
	public Card getCard(){
		try {
			return this.gameController.getCard();
		} catch (RemoteException e) {
			throw new PlayerDisconnected(this.name + " has left the game!");
		}
	}

	public void setMsg(String msg) throws RemoteException {
		gameController.setMsg(msg);
	}
}
