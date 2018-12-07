package main.uno;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import main.interfaces.gameControllerInterface;

public class Player {
	private String name;
	private gameControllerInterface gameController;
	private List<Card> cards;
	private int score;
	private boolean ready;

	public Player() {
		cards = new ArrayList<>();
	}

	public Player(String name, gameControllerInterface gameController) {
		this.name = name;
		this.gameController = gameController;
		this.score = 0;
		cards = new ArrayList<>();
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

	public void sendMessage(String message) {
		try {
			this.gameController.setMsg(message);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public boolean isReady() {
		return ready;
	}

	public void setCardAmountPlayer(String name, int size) throws RemoteException {
		this.gameController.setCardAmountPlayer(name, size);
	}

	public void addCards(List<Card> draw) {
		try {
			this.gameController.addCards(draw);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public void addPile(Card card) {
		try {
			this.gameController.addPile(card);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
}
