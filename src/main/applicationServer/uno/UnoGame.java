package main.applicationServer.uno;

import java.rmi.RemoteException;
import java.util.*;
import java.util.stream.IntStream;

import main.applicationServer.PlayerInterface;
import main.client.GameInfo;
import main.exceptions.GamePlayError;
import main.exceptions.WrongCardOnPileException;
import main.interfaces.dbInterface;

public class UnoGame {
	private List<PlayerInterface> players;
	private List<Card> pile;
	private List<Card> deck;

	private int myPlayDirection;
    private int currentPlayer;
	private int playerCount;

	private String name;
    private String gameId;
    private String winner;

    public int getPlayerCount() {
		return playerCount;
	}

	public List<Card> getDeck() {
		return deck;
	}

	public UnoGame(GameInfo gameInfo) {
		this.name = gameInfo.getGameName();
		this.players = new ArrayList<>();
		this.playerCount = gameInfo.getAmountOfPlayers();

		deck = new ArrayList<>();
		pile = new ArrayList<>();
		players = new ArrayList<>();
		newDeck();
	}

	public PlayerInterface getNextPlayer(int skip) {
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
			cards.add(getCardFromDeck());
		}
		return cards;
	}

    private Card getCardFromDeck() {
        Card toRemove = deck.stream()
                .findFirst()
                .orElseGet(() ->{
                    newDeck();
                    return getCardFromDeck();
                });
        deck.remove(toRemove);
        return toRemove;
    }

    private void dealCards() {
		Card card = getCardFromDeck();
		players.forEach(player -> player.addCards(draw(7)));
		pile.add(0, card);
		players.forEach(player -> player.addPile(card));
		initNewGame(card);
	}

	private void initNewGame(Card card) {
		currentPlayer = 0;
		myPlayDirection = 1;

		if (card.getClass() == WildCard.class || card.getClass() == WildDrawCard.class) {
			card.myColour = CardColours.BLUE;
		}
	}

	private String playTurn() {
		try {
			PlayerInterface currentPlayer = players.get(this.currentPlayer);
			Card card = currentPlayer.getCard();
			if (card == null) {
				addCardToPlayer(currentPlayer);
				return null;
			}
			if (!pile.get(0).canPlayOn(card)) {
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

	private void addCardToPlayer(PlayerInterface currentPlayer) {
		currentPlayer.addCards(draw(1));
		tellPlayerHandSize(currentPlayer);
		goToNextPlayer();
	}

	public void playCard(PlayerInterface player, Card card) throws RemoteException {
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
            updateAllPlayers(player, card);
        } else {
			player.addCards(draw(1));
			System.out.println("An error occured (a card was played when it was not part of the player's hand)");
		}

	}

    private void updateAllPlayers(PlayerInterface player, Card card) {
        for (PlayerInterface iter : players) {
            iter.addPile(card);
            iter.setCardAmountPlayer(player.getName(), player.getCards().size());
        }
    }

    public void play() {
		dealCards();
		players.forEach(this::tellPlayerHandSize);

        winner = playTurn();
		while (winner == null) {
			winner = playTurn();
		}

		players.forEach(player -> {
		    player.setScore();
		    player.setPlayerNotReady();
        });
	}

	public String getWinner() {
        return winner;
    }

	private void tellPlayerHandSize(PlayerInterface player) {
		players.forEach(player1 -> {
		    player1.setCardAmountPlayer(player.getName(), player.getCards().size());
        });
	}

	public String getId() {
		return this.gameId;
	}

	public String getName() {
		return this.name;
	}

	public void addPlayer(PlayerInterface player) {
		players.add(player);
		if (players.stream().allMatch(PlayerInterface::isReady) && playerCount == players.size()) {
			this.play();
		}
	}


	public void sendMsg(String msg){
		for (PlayerInterface player : players) {
			player.sendMessage(msg);
		}

	}

	public void newDeck() {
		Arrays.asList(CardColours.getColours()).forEach(this::addCards);
		Collections.shuffle(deck);
	}

    private void addCards(CardColours cardColours) {
        addSpecialCards(cardColours);
        addNormalCards(cardColours);
    }

    private void addNormalCards(CardColours c) {
        IntStream.rangeClosed(1, 9).forEach(i -> {
            createNormalCards(c, i);
        });
	}

    private void createNormalCards(CardColours c, int i) {
        deck.add(new Card(c, i));
        deck.add(new Card(c, i));
    }

    private void addSpecialCards(CardColours c) {
		deck.add(new Card(c, 0));
		deck.add(new SkipCard(c, 1));
		deck.add(new SkipCard(c, 1));
		deck.add(new ReverseCard(c));
		deck.add(new ReverseCard(c));
		deck.add(new DrawCard(c, 2));
		deck.add(new DrawCard(c, 2));
		deck.add(new WildDrawCard(4));
		deck.add(new WildCard());
	}

	public List<PlayerInterface> getPlayers() {
		return players;
	}

	public void endGame() {
        sendMsg("Someone has left the Game, the game has ended");
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

	private void startGame() {
        new Thread(this::play).start();
	}

    public void reversePlayDirection() {
        this.myPlayDirection = this.myPlayDirection * (-1);
    }

    public int connectedPlayers() {
        return this.players.size();
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
}
