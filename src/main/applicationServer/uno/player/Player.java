package applicationServer.uno.player;

import java.rmi.RemoteException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import applicationServer.uno.cards.properties.CardColours;
import applicationServer.uno.cards.Card;
import client.UserInfo;
import exceptions.NotInPlayersHand;
import exceptions.PlayerDisconnected;
import interfaces.gameControllerInterface;

public class Player implements PlayerInterface {
	private UserInfo userInfo;
	private gameControllerInterface gameController;
	private List<Card> cards;
	private int score;
	private boolean ready;
	private Deque<Card> pile;

	public Player(UserInfo userInfo, gameControllerInterface gameController) {
		this.userInfo = userInfo;
		this.score = 0;
		this.gameController = gameController;
		this.pile = new ArrayDeque<>();
		cards = new ArrayList<>();
	}

	public String getName() {
		return userInfo.getUsername();
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
			throw new PlayerDisconnected(this.userInfo + " has left the game!");
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
	public String getLog() {
		return "";
	}

	@Override
	public Deque<Card> getPile() {
		return pile;
	}

	@Override
	public void setCardAmountPlayer(String name, int size) {
		try {
			this.gameController.setCardAmountPlayer(name, size);
		} catch (RemoteException e) {
			throw new PlayerDisconnected(this.userInfo + " has left the game!");
		}
	}

	@Override
	public void addCards(List<Card> draw) {
		try {
			this.cards.addAll(draw);
			this.gameController.addCards(draw);
		} catch (RemoteException e) {
			throw new PlayerDisconnected(this.userInfo + " has left the game!");
		}
	}

	@Override
	public void addPile(Card card) {
		try {
			this.pile.push(card);
			this.gameController.addPile(card);
		} catch (RemoteException e) {
			throw new PlayerDisconnected(this.userInfo + " has left the game!");
		}
	}

	@Override
	public Card getCard(){
		try {
			Card card = this.gameController.getCard();
			if (card == null) return null;
			removeCard(card);
			System.out.println("My cardamount: " + cards.size());
			return card;
		} catch (RemoteException e) {
			throw new PlayerDisconnected(e.getMessage());
		}
	}

	private void removeCard(Card card) {
		Card pickedCard = cards.stream()
				.filter(card1 -> card1.cardName.equals(card.cardName))
				.findFirst()
				.orElseThrow(() -> new NotInPlayersHand("An error occured (a card was played when it was not part of the player's hand) " + card.toString()));
		this.cards.remove(pickedCard);
	}

	public void setMsg(String msg) throws RemoteException {
		gameController.setMsg(msg);
	}


}
