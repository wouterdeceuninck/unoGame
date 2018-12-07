package main.uno;

import java.rmi.RemoteException;
import java.util.*;

import main.controller.NewGameInfo;
import main.exceptions.GamePlayError;
import main.exceptions.UnknownUserToGameException;
import main.exceptions.WrongCardOnPileException;
import main.interfaces.dbInterface;
import main.interfaces.gameControllerInterface;

public class UnoGame {
	private List<Player> players;
	private List<Card> pile;
	private List<Card> deck;

	private int myPlayDirection, currentPlayer;
	private int playerCount;
	private dbInterface db;

	private String name;
	private String gameId;

	public String getGameId() {
		return gameId;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public void setPlayDirection(int i) {
		this.myPlayDirection = i;
	}

	public void reversePlayDirection() {
		this.myPlayDirection = this.myPlayDirection * (-1);
	}

	public int connectedPlayers() {
		return players.size();
	}

	public int getPlayerCount() {
		return playerCount;
	}

	public UnoGame(NewGameInfo newGameInfo) {
		this.name = newGameInfo.getGameName();
		this.players = new ArrayList<>();
		this.playerCount = newGameInfo.getNumberOfPlayers();

		deck = new ArrayList<>();
		pile = new ArrayList<>();
		players = new ArrayList<>();

		newDeck();
	}

	public Player getNextPlayer(int skip) {
		int i = currentPlayer + (skip * myPlayDirection);
		i = i % players.size();
		if (i < 0) {
			i += players.size();
		}

		return players.get(i);
	}


	public void goToNextPlayer() {
		currentPlayer = (currentPlayer + myPlayDirection + players.size()) % players.size();
	}

	public List<Card> draw(int nCards) {
		List<Card> cards = new ArrayList<>();
		for (int i = 0; i < nCards; i++) {
			if (deck.size() == 0 && pile.size() > 0) {
				Card keep = pile.get(0);
				deck.addAll(pile.subList(1, pile.size()));
				pile.clear();
				pile.add(keep);
				Collections.shuffle(deck);
			}
			cards.add(deck.remove(0));
		}
		return cards;
	}

	private void dealCards() {
		Card card = deck.remove(0);
		players.forEach(player -> player.addCards(draw(7)));
		pile.add(0, card);
		players.forEach(player -> player.addPile(card));
		initNewGame(card);
	}

	private void initNewGame(Card card) {
		currentPlayer = 0;
		myPlayDirection = 1;

		if (card.getClass() == WildCard.class || card.getClass() == WildDrawCard.class) {
			card.myColour = Card.COLOUR_BLUE;
		}
	}

	private String playTurn() {
		try {
			Player currentPlayer = players.get(this.currentPlayer);
			Card card = currentPlayer.getGameController().getCard();
			if (card == null) {
				addCardToPlayer(currentPlayer);
				return null;
			}
			if (!card.canPlayOn(pile.get(0))) {
				throw new WrongCardOnPileException("Cannot play " + card.toString() + " on " + pile.get(0));
			}

			pile.add(0, card);
			playCard(currentPlayer, card);

			if (currentPlayer.getCards().size() == 0) {
				return currentPlayer.getName();
			}

			goToNextPlayer();
			return null;
		} catch (RemoteException e) {
			e.printStackTrace();
			return null;
		}
	}

	private String addCardToPlayer(Player currentPlayer) {
		currentPlayer.addCards(draw(1));
		tellPlayerHandSize(currentPlayer);
		goToNextPlayer();
		return null;
	}

	public void playCard(Player player, Card card) throws RemoteException {
		boolean valid = false;
		Iterator<Card> newIterator = player.getCards().iterator();
		while (newIterator.hasNext()) {
			Card c = newIterator.next();
			if (c.cardName.equals(card.cardName)) {
				valid = true;
				player.getCards().remove(c);
				break;
			}
		}
		if (valid) {
			for (Player iter : players) {
				iter.getGameController().addPile(card);
				iter.getGameController().setCardAmountPlayer(player.getName(), player.getCards().size());
				;
			}
		} else {
			player.getGameController().addCards(draw(1));
			System.out.println("An error occured (a card was played when it was not part of the player's hand)");
		}

	}

	public void play() {
		dealCards();
		players.forEach(this::tellPlayerHandSize);

		String winner = playTurn();
		while (winner == null) {
			winner = playTurn();
		}

		players.forEach(player -> player.setScore());
		playIfPlayersReady();
	}

	private void tellPlayerHandSize(Player player) {
		players.forEach(player1 -> {
            try {
                player1.setCardAmountPlayer(player.getName(), player.getCards().size());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        });
	}

	public String getId() {
		return this.gameId;
	}

	public void draw(gameControllerInterface nextPlayer, int nDraw) throws RemoteException {
		nextPlayer.addCards(draw(nDraw));
	}

	public String getName() {
		return this.name;
	}

	public void addPlayer(Player player) {
		players.add(player);
		if (players.stream().allMatch(Player::isReady)) {
			this.play();
		}
	}


	public void sendMsg(String msg) throws RemoteException {
		for (Player player : players) {
			player.getGameController().setMsg(msg);
		}

	}

	private void newDeck() {
		for (int c = 1; c <= 4; c++) {
			deck.add(new Card(c, 0));
			deck.add(new SkipCard(c, 1));
			deck.add(new SkipCard(c, 1));
			deck.add(new ReverseCard(c));
			deck.add(new ReverseCard(c));
			deck.add(new DrawCard(c, 2));
			deck.add(new DrawCard(c, 2));
			deck.add(new WildDrawCard(4));
			deck.add(new WildCard());

			for (int i = 1; i <= 9; i++) {
                deck.add(new Card(c, i));
                deck.add(new Card(c, i));
            }
		}
		Collections.shuffle(deck);
	}

	public List<Player> getPlayers() {
		return players;
	}

	public void endGame() {
		try {
			sendMsg("Someone has left the Game, the game has ended");
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}

	public List<Card> getCardsFromPlayer(String username) throws GamePlayError {
		return getPlayerCards(username);
	}

	private List<Card> getPlayerCards(String username) throws GamePlayError {
		return players.stream().filter(player -> player.getName().equals(username))
				.findFirst()
				.orElseThrow(() -> new GamePlayError("An error in the game occured"))
				.getCards();
	}

	private void playIfPlayersReady() {
		if (players.stream().allMatch(Player::isReady)) startGame();
	}


	private void startGame() {
		Thread thread = new Thread(() -> {
            try {
                db.createPlayerHandTabel(this.gameId);
                this.play();
            } catch (RemoteException e) {
				e.printStackTrace();
			}
		});
		thread.start();
	}

}
