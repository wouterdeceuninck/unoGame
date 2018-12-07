package main.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import main.client.UserController;
import main.exceptions.NoCardsFoundExcepion;
import main.interfaces.gameControllerInterface;
import main.interfaces.ServerInterface;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import main.uno.Card;

public class GameController extends UnicastRemoteObject implements gameControllerInterface {

    private String path = ".\\resources\\pictures\\";

	private GameLogic gameLogic;
	private UserController userController;

	// class variables
	private String username;
	private ObservableList data = FXCollections.observableArrayList();
	private List<ImageView> cards = new ArrayList<>();

	// game variables
	private String currentPlayer;
	private ListView scoreBoard;
	private List<Card> cardsList;
	private List<Opponent> opponents;
	private boolean readyToStart, boolDrawCard, colourSelected;

	private int selectedColor;

	// back-image
	private Image backImage;
	private Card selectedCard;

	// fxml variables
	@FXML
	private Label title;

	@FXML
	private Button btn_red, btn_green, btn_blue, btn_yellow, btn_exit;

	@FXML
	private TextField opponent1, opponent2, opponent3;

	@FXML
	private ImageView image_lastcard;

	@FXML
	private VBox opponent2Box, opponent3Box;

	@FXML
	private HBox opponent1Box, userBox;

	@FXML
	private ImageView btn_drawCard;

	// chat variables
	@FXML
	private TextField chat_input;

	@FXML
	private TextArea chat_output, text_scoreboard;
    private String nextPlayer;

    public GameController(UserController userController, ServerInterface server) throws RemoteException, FileNotFoundException {
	    this.userController = userController;
		this.gameLogic = new GameLogic(server, userController.getGameInfo(), userController.getUserInfo().getUsername());

        cardsList = new ArrayList<>();
        opponents = new ArrayList<>();
        backImage = new Image(new FileInputStream(path + userController.getGameInfo().getGameTheme() + "\\" + "UNO-Back.png"));
    }

	public void initialize() throws RemoteException {
		this.readyToStart = false;
		title.setText(userController.getGameInfo().getGameName());
		btn_drawCard.setImage(backImage);
		btn_drawCard.setFocusTraversable(true);
		btn_drawCard.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            if (!readyToStart) {
                gameLogic.startGame(username);
                this.readyToStart = true;
            }
            event.consume();
		});

		btn_red.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			this.colourSelected = true;
			this.selectedColor = Card.COLOUR_RED;
			event.consume();
		});

		btn_blue.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			this.colourSelected = true;
			this.selectedColor = Card.COLOUR_BLUE;
			event.consume();
		});

		btn_yellow.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			this.colourSelected = true;
			this.selectedColor = Card.COLOUR_YELLOW;
			event.consume();
		});

		btn_green.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			this.colourSelected = true;
			this.selectedColor = Card.COLOUR_GREEN;
			event.consume();
		});

		btn_drawCard.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			boolDrawCard = true;
			event.consume();
		});

	}

	@FXML
	public void sendMsg() throws RemoteException {
	    gameLogic.sendGameMsg(chat_input.getText());
		chat_input.setText("");
	}

	@FXML
	public void enteredOpaque() {

	}

	@Override
	public void setMsg(String msg) {
		String message = chat_output.getText() + msg + "\n";
		chat_output.setText(message);
	}

	@Override
	public void setScoreboard(List<String> scoreboard) {
		text_scoreboard.clear();
		scoreboard.forEach(score -> text_scoreboard.setText(score + "\n"));
	}

	public void addPlayer(String username) {
        opponents.add(new Opponent(username, 0, 0, opponents.size()));
	}

	private void setOpponentCards(Opponent opponent) throws  NoCardsFoundExcepion{
        ImageView imageView = getImageView(opponent, OpponentGUISide.NORD);

		if (opponent.getId() == 1) {
			opponent1Box.getChildren().clear();
            opponent1Box.getChildren().addAll(Optional.ofNullable(imageView).orElseThrow(() -> new NoCardsFoundExcepion("There were no Cards found!")));
        }
		if (opponent.getId() == 2) {
			opponent2Box.getChildren().clear();
            opponent2Box.getChildren().addAll(Optional.ofNullable(imageView).orElseThrow(() -> new NoCardsFoundExcepion("There were no Cards found!")));
        }
		if (opponent.getId() == 3) {
			opponent3Box.getChildren().clear();
            opponent3Box.getChildren().addAll(Optional.ofNullable(imageView).orElseThrow(() -> new NoCardsFoundExcepion("There were no Cards found!")));
		}
	}

    private ImageView getImageView(Opponent opponent, OpponentGUISide opponentGUISide) {
        ImageView imageView1 = null;
        for (int i = 0; i < opponent.getAmountCards(); i++) {
            imageView1 = new ImageView(backImage);
            imageView1.setFitHeight(opponentGUISide.getFitHeightSize());
            imageView1.setPreserveRatio(true);
            imageView1.setRotate(opponentGUISide.getRotationDegrees());
        }
        return imageView1;
    }

	private void setMyCards() {
        userBox.getChildren().clear();
        gameLogic.getGameData().getCards().forEach((Card card) -> {
            setASingleCardInBox(card);
        });
	}

    private void setASingleCardInBox(Card card) {
        try {
            ImageView imageView = new ImageView(
                    new Image(new FileInputStream(path + userController.getGameInfo().getGameTheme() + "\\" + card.cardName), 0, 125, true, true));
            imageView.setFocusTraversable(true);
            imageView.setFitHeight(125);
            imageView.setPreserveRatio(true);
            imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
                for (int i = 0; i < userBox.getChildren().size(); i++) {
                    if (userBox.getChildren().get(i) == event.getTarget()) {
                        selectedCard = cardsList.get(i);
                        break;
                    }
                }
                event.consume();
            });
            userBox.getChildren().add(imageView);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
	public void addCards(List<Card> cards) throws RemoteException {
	    gameLogic.addCards(cards);
		Platform.runLater(this::setMyCards);
	}

	@Override
	public Card getCard() {
		this.setMsg("It is your turn, play a card!");

        Optional<Card> pickedCard = Optional.ofNullable(getCardRec());
        return pickedCard.isPresent() ? removeCard(pickedCard.get()) : null;
	}

    private Card removeCard(Card pickedCard) {
        gameLogic.removeCard(pickedCard);
        Platform.runLater(this::setMyCards);
        return pickedCard;
    }

    public Card getCardRec() {
		selectedCard = null;
		boolDrawCard = false;
		while (selectedCard == null && !boolDrawCard) {
			try {
				Thread.sleep(100L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		if (boolDrawCard) {
			return null;
		}
		if (!selectedCard.canPlayOn(gameLogic.getGameData().getTopCard())) {
			return getCardRec();
		}
		return selectedCard;
	}

	@Override
	public void setNextPlayer(String username) {
		nextPlayer = username;
	}

	@Override
	public void addPile(Card card) {
		gameLogic.setTopCard(card);
		try {
			image_lastcard.setImage(new Image(new FileInputStream(path + gameLogic.getGameInfo().getGameTheme() + "\\" + card.cardName)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setCardAmountPlayer(String username, int amount) {
        setOpponentsAmountOfCards(username, amount);
    }

    private void setOpponentsAmountOfCards(String username, int amount) {
        Platform.runLater(() -> setOpponentsCards(username, amount));
    }

    private void setOpponentsCards(String username, int amount) {
        for (Opponent opponent : opponents) {
            if (opponent.getName().equals(username)) {
                opponent.setAmountCards(amount);
                setOpponentCards(opponent);
            }
        }
    }

    @Override
	public void setReady(boolean b) throws RemoteException {
		this.readyToStart = b;
		this.cardsList.clear();
		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				userBox.getChildren().clear();
				try {
					btn_drawCard.setImage(new Image(new FileInputStream(path + gameLogic.getGameInfo().getGameTheme() + "\\" + "UNO-Back.png")));
					if (!b) {
						opponent1Box.getChildren().clear();
						opponent2Box.getChildren().clear();
						opponent3Box.getChildren().clear();
					}
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});

	}

	@Override
	public int askColor() throws RemoteException {
		setMsg("Chose a color!");
		btn_blue.setOpacity(1);
		btn_green.setOpacity(1);
		btn_red.setOpacity(1);
		btn_yellow.setOpacity(1);
		this.selectedColor = Card.COLOUR_BLUE;
		this.colourSelected = false;
		while (!colourSelected) {
			try {
				Thread.sleep(100L);
			} catch (Exception e) {
			}
		}

		btn_blue.setOpacity(0.7);
		btn_green.setOpacity(0.7);
		btn_red.setOpacity(0.7);
		btn_yellow.setOpacity(0.7);

		return selectedColor;
	}

	@Override
	public void sendPlayerInfo(ArrayList<String> info) {
		if (opponents.isEmpty()) {
			Platform.runLater(() -> {
                info.stream()
                        .filter(names -> !names.equals(username))
                        .forEach(this::addPlayer);
            });
		}
	}
}


