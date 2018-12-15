package main.applicationServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import main.applicationServer.security.Token;
import main.client.GameInfo;
import main.exceptions.GameNotFoundException;
import main.exceptions.GamePlayError;
import main.exceptions.UnAutherizedException;
import main.exceptions.UsernameAlreadyUsedException;
import main.interfaces.dispatcherInterface;
import main.interfaces.gameControllerInterface;
import main.interfaces.ServerInterface;
import main.applicationServer.uno.Card;
import main.applicationServer.uno.Player;
import main.applicationServer.uno.UnoGame;

public class ServerInterfaceImpl extends UnicastRemoteObject implements ServerInterface {
	public static Logger logger = Logger.getLogger(ServerInterfaceImpl.class.getName());

	private List<UnoGame> games;
	private int serverPortNumber;
	private List<Player> players;
	private dispatcherInterface dispatcher;

	public ServerInterfaceImpl(int serverPortNumber) throws RemoteException {
		this.games = new ArrayList<>();
		this.players = new ArrayList<>();
		this.serverPortNumber = serverPortNumber;
	}

	@Override
	public String register(String username, String password) throws RemoteException, UsernameAlreadyUsedException {
        players.add(new Player(username));
		return getNewToken(username);
    }

    @Override
	public String login(String username, String password) throws RemoteException, UnAutherizedException {
//		if (db.loginUser(username, password)) {
			players.add(new Player(username));
            return getNewToken(username);
//        } else {
//            throw new UnAutherizedException("Combination of username and password does not exist!");
//        }
	}

	private String getNewToken(String username) {
		return (new Token.Builder()
                        .setName(username)
                        .setTimestamp(LocalDateTime.now().toString())
                        .setAlg("SHA-256")
                        .build())
                .toString();
	}


	public List<String> getGames() {
		return games.stream().map(this::getGameInfo)
				.collect(Collectors.toList());
	}

	private String getGameInfo(UnoGame game) {
		return game.getId()
				+ "\t" + game.getName()
				+ "\t" + game.connectedPlayers()
				+ "/" + game.getPlayerCount();
	}

	@Override
	public void sendToAllPlayers(String message, String username) {
		players.forEach(player -> player.sendMessage(message));
	}

	@Override
	public String startNewGame(GameInfo gameInfo) throws RemoteException {
		UnoGame uno = new UnoGame(gameInfo);
		games.add(uno);
		String gameId = serverPortNumber + ":" + games.size();
		uno.setGameId(gameId);
		return gameId;
	}

	@Override
	public void joinGame(gameControllerInterface gameController, String gameID, String username) throws IllegalStateException {
        try {
			Player player = findPlayer(username);
			player.setGameControllerInterface(gameController);
			getGameByID(gameID).addPlayer(player);
			players.remove(player);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private UnoGame getGameByID(String gameID) {
		return games.stream().filter(game -> game.getId().equals(gameID))
				.findFirst().orElseThrow(() -> new GameNotFoundException("There is no game with this ID"));
	}

	private Player findPlayer(String username) {
		return players.stream().filter(player -> player.getName().equals(username))
				.findFirst().orElseThrow(() -> new UnAutherizedException("the user is not known to the server"));
	}

	@Override
	public void sendGameMsg(String msg, int gameID, String username) throws RemoteException {
		games.get(gameID).sendMsg(username + ": " + msg);
	}

    @Override
	public List<Card> getCards(String username, String gameID) {
        return getForId(gameID).getCardsFromPlayer(username);
	}

    private UnoGame getForId(String gameID) {
        return games.stream().filter(game -> game.getId().equals(gameID))
            .findFirst()
            .orElseThrow(() -> new GamePlayError("Game not found"))    ;
    }

	@Override
	public void openLobby(gameControllerInterface gci) {
		for (UnoGame game : games) {
			for (PlayerInterface player : game.getPlayers()) {
				if (gci.hashCode()==player.hashCode()) {
					game.endGame();
				}
			}
		}
	}



	//	public void setdb(int dbPortnumber) {
//        try {
//            this.db = (dbInterface) LocateRegistry.getRegistry("localhost", dbPortnumber).lookup("UNOdatabase"+ dbPortnumber);
//		} catch (RemoteException | NotBoundException e) {
//			e.printStackTrace();
//		}
//    }
	public void getDispatcher() {
		try {
			dispatcher = (dispatcherInterface) LocateRegistry.getRegistry("localhost", 1099).lookup("UNOdispatcher");
		} catch (RemoteException|NotBoundException e) {
			e.printStackTrace();
		}
	}
}