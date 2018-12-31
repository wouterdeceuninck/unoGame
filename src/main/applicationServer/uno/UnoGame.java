package applicationServer.uno;

import java.rmi.RemoteException;
import java.util.*;

import applicationServer.uno.cards.properties.CardColours;
import applicationServer.uno.cards.properties.CardSymbol;
import applicationServer.uno.player.PlayerInterface;
import applicationServer.uno.cards.*;
import client.GameInfo;
import exceptions.GamePlayError;
import exceptions.WrongCardOnPileException;

public class UnoGame {
	private List<PlayerInterface> players;
	private Deque<Card> pile;
	private List<Card> deck;

	private int myPlayDirection;
    private int currentPlayer;
	private int playerCount;

	private String name;
    private String gameId;
    private String winner;
    private boolean readyToStart = false;

    public int getPlayerCount() {
		return playerCount;
	}

	public List<Card> getDeck() {
		return deck;
	}

	public UnoGame(GameInfo gameInfo) {
    	this.gameId = gameInfo.getGameID();
		this.name = gameInfo.getGameName();
		this.players = new ArrayList<>();
		this.playerCount = gameInfo.getAmountOfPlayers();
		this.gameId = gameInfo.getGameID();

		deck = new ArrayList<>();
		pile = new ArrayDeque<>();
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
		pile.push(card);
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
			if (!card.canPlayOn(pile.peek())) {
				throw new WrongCardOnPileException("Cannot play " + card.toString() + " on " + pile.peek());
			}

			pile.push(card);
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

	private void playCard(PlayerInterface player, Card card) throws RemoteException {
		player.getCards().remove(
		        player.getCards().stream()
                    .filter(card1 -> card1.equals(card))
                    .findFirst()
                    .orElseThrow(() -> new NotInPlayersHand("An error occured (a card was played when it was not part of the player's hand)")));
		pile.push(card);
        updateAllPlayers(player, card);
	}

    private void updateAllPlayers(PlayerInterface player, Card card) {
        for (PlayerInterface iter : players) {
            iter.addPile(card);
            iter.setCardAmountPlayer(player.getName(), player.getCards().size());
        }
    }

    public String play() {
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
		return winner;
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
			this.readyToStart = true;
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

    private void addNormalCards(CardColours cardColours) {
        for (CardSymbol cardSymbol: CardSymbol.getNormalSymbols()){
        	createNormalCards(cardColours, cardSymbol);
		}
	}

    private void createNormalCards(CardColours c, CardSymbol cardSymbol) {
        deck.add(new Card(c, cardSymbol));
        deck.add(new Card(c, cardSymbol));
    }

    private void addSpecialCards(CardColours c) {
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
        sendMsg("Someone has left the GameObject, the game has ended");
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

	public void startGame() {
        new Thread(this::play).start();
	}

    public void reversePlayDirection() {
        this.myPlayDirection = this.myPlayDirection * (-1);
    }

    public int connectedPlayers() {
        return this.players.size();
    }

	public boolean readyToStart() {
		return this.readyToStart;
	}

	private class NotInPlayersHand extends RuntimeException {
        public NotInPlayersHand(String message) {
            super(message);
        }
    }
}
