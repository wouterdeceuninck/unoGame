package applicationServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import applicationServer.uno.player.BotPlayer;
import applicationServer.uno.player.PlayerInterface;
import client.businessObjects.GameInfo;
import databaseServer.DbInterface;
import databaseServer.security.JWTVerifier;
import databaseServer.security.Token;
import databaseServer.security.util.JWTmapper;
import exceptions.*;
import dispatcher.DispatcherInterface;
import interfaces.gameControllerInterface;
import applicationServer.uno.player.Player;
import applicationServer.uno.UnoGame;

public class ServerInterfaceImpl extends UnicastRemoteObject implements ServerInterface {
	private List<UnoGame> games;
	private int serverPortNumber;
	private JWTVerifier JWTVerifier;
	private DbInterface dbInterface;
	private DispatcherInterface dispatcher;
	private final int MAXSERVERLOAD = 20;
	private List<GameInfo> activeGamesList;
	private String activeGamesListLastUpdateTime;

	public ServerInterfaceImpl(int serverPortNumber, int dbPortnumber) throws RemoteException {
		this.games = new ArrayList<>();
		this.serverPortNumber = serverPortNumber;
		this.JWTVerifier = new JWTVerifier();
		this.activeGamesList = new ArrayList<>();
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

	@Override
	public int getAmountOfGameOnServer() throws RemoteException {
		return games.size();
	}

	public List<GameInfo> getGames(String token) throws RemoteException {
		verifyToken(token);

		if (null == activeGamesListLastUpdateTime || activeGamesListLastUpdateTime.isEmpty()) {
			List<GameInfo> activeGamesFromDB = dbInterface.getActiveGames();
			if (setActiveGamesList(activeGamesFromDB));
			setGameIDs();
			return this.activeGamesList;
		}
		List<GameInfo> gamesFromDBServer = dbInterface.getActiveGames(activeGamesListLastUpdateTime);
		setActiveGamesList(gamesFromDBServer);
		setGameIDs();

		return this.activeGamesList;
	}

	private void setGameIDs() {
		activeGamesList.forEach(gameInfo -> gameInfo.setGameID(gameInfo.getServerPortnumber() + "_" + gameInfo.getGameID()));
	}

	private boolean setActiveGamesList(List<GameInfo> activeGamesFromDB) {
		if (null != activeGamesFromDB && !activeGamesFromDB.isEmpty()) {
			this.activeGamesList = activeGamesFromDB;
			this.activeGamesListLastUpdateTime = LocalDateTime.now().toString();
			return true;
		}
		return false;
	}

	@Override
	public String startNewGame(GameInfo gameInfo, String token) throws RemoteException {
		verifyToken(token);
		if(serverFull()) {
			return connectToApplicationServer(dispatcher.getLeastLoadedApplicationServer()).startNewGame(gameInfo, token);
		}
		String game_id = dbInterface.addGame(gameInfo, this.serverPortNumber);
		gameInfo.setGameID(game_id);
		games.add(new UnoGame(gameInfo));
		return this.serverPortNumber + "_" + game_id;
	}

	private ServerInterface connectToApplicationServer(int leastLoadedApplicationServer) throws RemoteException{
		try {
			return (ServerInterface) LocateRegistry.getRegistry("localhost", leastLoadedApplicationServer).lookup("UNOserver");
		} catch (NotBoundException e) {
			e.printStackTrace();
			throw new RemoteException();
		}
	}

	private boolean serverFull() {
		return games.size() >= MAXSERVERLOAD;
	}

	@Override
	public boolean joinGame(gameControllerInterface gameController, String gameID, String token) throws IllegalStateException, GameNotFoundException {
		String username = verifyToken(token);
		String[] serverPortAndID = gameID.split("_");
		int serverport = Integer.parseInt(serverPortAndID[0]);
		String game_id = serverPortAndID[1];

		if (this.serverPortNumber != serverport) throw new RerouteNeededExeption(serverport);

		try {
			PlayerInterface player = new Player(username, gameController);
			if (!dbInterface.addUsersToGame(game_id)) throw new GameFullException("The game is already full!");
			UnoGame unoGame = getGameByID(game_id);
			unoGame.addPlayer(player);
			startWhenReady(game_id, unoGame);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
        return true;
	}

	@Override
	public boolean leaveGame(String gameID, String token) throws GameNotFoundException {
		String username = verifyToken(token);
		String game_id = gameID.split("_")[1];

		try {
			dbInterface.removeUsersFromGame(game_id);

			UnoGame unoGame = getGameByID(game_id);
			if (unoGame == null) return false;
			unoGame.removePlayer(username);
		} catch (RemoteException e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public boolean joinGameAddBot(String gameID, String token) throws GameNotFoundException, GameFullException {
		verifyToken(token);
		String[] serverPortAndID = gameID.split("_");
		int serverport = Integer.parseInt(serverPortAndID[0]);
		String game_id = serverPortAndID[1];
		try {
			if (!dbInterface.addUsersToGame(game_id)) throw new GameFullException("The game is already full!");
			UnoGame unoGame = getGameByID(game_id);
			unoGame.addPlayer(new BotPlayer("BotPlayer" + unoGame.connectedPlayers()));
			startWhenReady(game_id, unoGame);
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
	public void sendGameMsg(String msg, String gameID, String token) throws RemoteException, GameNotFoundException {
		String username = verifyToken(token);
		String game_id = getGameID(gameID);
		getGameByID(game_id).sendMsg(username + ": " + msg);
	}

	private String verifyToken(String token) {
		Token tokenObject = new Token(token);
		if (!JWTVerifier.verify(tokenObject)) throw new UnAutherizedException("The token was not valid!");
		return JWTmapper.getUsername(tokenObject);
	}

	private String getGameID(String gameID) {
		return gameID.split("_")[1];
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