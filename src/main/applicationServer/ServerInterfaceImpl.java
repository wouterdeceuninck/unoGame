package applicationServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

import applicationServer.uno.player.BotPlayer;
import applicationServer.uno.player.PlayerInterface;
import client.GameInfo;
import databaseServer.DbInterface;
import exceptions.*;
import dispatcher.DispatcherInterface;
import interfaces.gameControllerInterface;
import applicationServer.uno.cards.Card;
import applicationServer.uno.player.Player;
import applicationServer.uno.UnoGame;

public class ServerInterfaceImpl extends UnicastRemoteObject implements ServerInterface {
	private List<UnoGame> games;
	private List<Player> players;
	private int serverPortNumber;
	private DbInterface dbInterface;
	private DispatcherInterface dispatcher;
	private final int MAXSERVERLOAD = 20;

	public ServerInterfaceImpl(int serverPortNumber, int dbPortnumber) throws RemoteException {
		this.games = new ArrayList<>();
		this.players = new ArrayList<>();
		this.serverPortNumber = serverPortNumber;
		setDbInterface(dbPortnumber);
		setDispatcherInterface();
	}

	@Override
	public String register(String username, String password) throws RemoteException, UsernameAlreadyUsedException {
		return dbInterface.registerUser(username, password);
    }

    @Override
	public String login(String username, String password) throws RemoteException, UnAutherizedException {
		return dbInterface.loginUser(username, password);
	}

	public List<GameInfo> getGames() throws RemoteException {
		return dbInterface.getActiveGames();
	}

	@Override
	public String startNewGame(GameInfo gameInfo) throws RemoteException {
		if(!serverFull()) return String.valueOf(dispatcher.getLeastLoadedApplicationServer());
		String game_id = dbInterface.addGame(gameInfo, this.serverPortNumber);
		gameInfo.setGameID(game_id);
		games.add(new UnoGame(gameInfo));
		return game_id;
	}

	private boolean serverFull() {
		return games.size() >= MAXSERVERLOAD;
	}

	@Override
	public boolean joinGame(gameControllerInterface gameController, String gameID, String username) throws IllegalStateException {
        try {
			PlayerInterface player = new Player(username, gameController);
			if (!dbInterface.addUsersToGame(gameID)) throw new GameFullException("The game is already full!");
			UnoGame unoGame = getGameByID(gameID);
			unoGame.addPlayer(player);
			startWhenReady(gameID, unoGame);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
        return true;
	}

	@Override
	public boolean joinGameAddBot(String gameID){
		try {
			if (!dbInterface.addUsersToGame(gameID)) throw new GameFullException("The game is already full!");
			UnoGame unoGame = getGameByID(gameID);
			unoGame.addPlayer(new BotPlayer("BotPlayer" + unoGame.connectedPlayers()));
			startWhenReady(gameID, unoGame);
		} catch (RemoteException e) {
			e.printStackTrace();
		}

		return true;
	}

	private void startWhenReady(String gameID, UnoGame unoGame) throws RemoteException {
		if(unoGame.readyToStart()) {
			dbInterface.setInactive(gameID);
			unoGame.startGame();
		}
	}

	private UnoGame getGameByID(String gameID) {
		return games.stream().filter(game -> game.getId().equals(gameID))
				.findFirst().orElseThrow(() -> new GameNotFoundException("There is no game with this ID"));
	}

	@Override
	public void sendGameMsg(String msg, String gameID, String username) throws RemoteException {
		getGameByID(gameID).sendMsg(username + ": " + msg);
	}

    @Override
	public List<Card> getCards(String username, String gameID) {
        return getForId(gameID).getCardsFromPlayer(username);
	}

    private UnoGame getForId(String gameID) {
        return games.stream().filter(game -> game.getId().equals(gameID))
            .findFirst()
            .orElseThrow(() -> new GamePlayError("GameObject not found"))    ;
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

	public void setDbInterface (int dbPortnumber) {
        try {
            this.dbInterface = (DbInterface) LocateRegistry.getRegistry("localhost", dbPortnumber).lookup("UNOdatabase"+ dbPortnumber);
		} catch (RemoteException | NotBoundException e) {
			e.printStackTrace();
		}
    }

	public void setDispatcherInterface() {
		try {
			dispatcher = (DispatcherInterface) LocateRegistry.getRegistry("localhost", 1099).lookup("UNOdispatcher");
		} catch (RemoteException|NotBoundException e) {
			e.printStackTrace();
		}
	}
}