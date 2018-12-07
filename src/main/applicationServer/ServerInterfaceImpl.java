package main.applicationServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.applicationServer.security.JwtFactory;
import main.applicationServer.security.Token;
import main.controller.NewGameInfo;
import main.exceptions.GamePlayError;
import main.exceptions.UnAutherizedException;
import main.exceptions.UsernameAlreadyUsedException;
import main.interfaces.clientInterface;
import main.interfaces.dbInterface;
import main.interfaces.dispatcherInterface;
import main.interfaces.gameControllerInterface;
import main.interfaces.lobbyInterface;
import main.interfaces.ServerInterface;
import main.uno.Card;
import main.uno.Player;
import main.uno.UnoGame;

public class ServerInterfaceImpl extends UnicastRemoteObject implements ServerInterface {




	public static Logger logger = Logger.getLogger(ServerInterfaceImpl.class.getName());

	private dbInterface db;
	private List<UnoGame> games;
	private int gameCounter, portnumber;
	private List<lobbyInterface> lobbies;
	private dispatcherInterface dispatcher;
	private boolean first = true;
	private JwtFactory jwtFactory = new JwtFactory();

	public ServerInterfaceImpl(int dbPortnumber, int portnumber) throws RemoteException {
		games = new ArrayList<>();
		lobbies = new ArrayList<>();
		gameCounter = 0;
		this.portnumber = portnumber;
		setdb(dbPortnumber);
	}

	@Override
	public String register(String username, String password) throws RemoteException, UsernameAlreadyUsedException {
        db.addUser(username, password);
        return getNewToken(username);
    }

    @Override
	public String login(String username, String password) throws RemoteException, UnAutherizedException {
		if (db.loginUser(username, password)) {
            return getNewToken(username);
        } else {
            throw new UnAutherizedException("Combination of username and password does not exist!");
        }
	}

	private String getNewToken(String username) {
		return (new Token.Builder()
                        .setName(username)
                        .setTimestamp(LocalDateTime.now().toString())
                        .setAlg("SHA-256")
                        .build(jwtFactory))
                .toString();
	}


	public void setdb(int dbPortnumber) {
        try {
            this.db = (dbInterface) LocateRegistry.getRegistry("localhost", dbPortnumber).lookup("UNOdatabase"+ dbPortnumber);
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}

    }

	public void getDispatcher() {
        try {
            dispatcher = (dispatcherInterface) LocateRegistry.getRegistry("localhost", 1099).lookup("UNOdispatcher");
		} catch (RemoteException|NotBoundException e) {
			e.printStackTrace();
		}
	}

	public List<String> getGames() throws RemoteException {
		try {
			List<String> gamesList = db.getActiveGames();
			games.forEach(game -> {
				gamesList.add(game.getId()
						+ "\t" + game.getName()
						+ "\t" + game.connectedPlayers()
						+ "/" + game.getPlayerCount());
			});
			return gamesList;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void send(String s, String username) throws RemoteException {
		String message;
		System.out.println(s);
        message = username + ": " + s;
		for (lobbyInterface temp : lobbies) {
            temp.setMsg(message);
		}

	}

	@Override
	public void startNewGame(NewGameInfo newGameInfo) throws RemoteException {
		UnoGame uno = new UnoGame(newGameInfo.getNumberOfPlayers(), newGameInfo.getGameName(), newGameInfo.getTheme(), db);
		games.add(uno);
		while (dispatcher==null) {
			getDispatcher();
		}
		uno.setGameId(db.addGame(gameCounter, newGameInfo.getGameName(), newGameInfo.getNumberOfPlayers(), this.portnumber, newGameInfo.getTheme()));
		dispatcher.updateInfo(portnumber, gameCounter);
		System.out.println("main.dispatcher was notified with the new info " + gameCounter);
		gameCounter++;
	}

	@Override
	public void giveLobby(lobbyInterface lobbyController) throws RemoteException {
		lobbies.add(lobbyController);
	}

	@Override
	public void exit(lobbyInterface lobbyController) throws RemoteException {
		lobbies.remove(lobbyController);
	}

	@Override
	public void joinGame(gameControllerInterface gameController, int gameID, String username)
			throws IllegalStateException {
        try {
			games.get(gameID).addPlayer(username, gameController);
		} catch (Exception e) {
			e.printStackTrace();
		}
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
			for (Player player : game.getPlayers()) {
				if (gci.hashCode()==player.getGameController().hashCode()) {
					game.endGame();
				}
			}
		}
	}


}