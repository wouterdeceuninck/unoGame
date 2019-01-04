package client.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import applicationServer.ServerInterface;
import applicationServer.uno.cards.properties.CardColours;
import applicationServer.uno.cards.properties.CardSymbol;
import client.businessObjects.GameInfo;
import client.businessObjects.UserInfo;
import exceptions.NoCardsFoundExcepion;
import interfaces.gameControllerInterface;
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
import applicationServer.uno.cards.Card;
import javafx.stage.Stage;

public class GameController extends UnicastRemoteObject implements gameControllerInterface {

	private final ServerInterface serverInterface;
	private final GameInfo gameInfo;
	private final UserInfo userInfo;
	private String path = "resources/pictures/";

	// game variables
	private ListView scoreBoard;
	private List<Card> cardsList;
	private List<Opponent> opponents;
	private boolean boolDrawCard, colourSelected;

	private CardColours selectedColor;

	// back-image
	private Image backImage;
	private Card selectedCard;
	private Deque<Card> pile;


	public GameController(UserInfo userInfo, GameInfo gameInfo, ServerInterface serverInterface) throws RemoteException {
		this.userInfo = userInfo;
		this.gameInfo = gameInfo;
		this.serverInterface = serverInterface;
		pile = new ArrayDeque<>();

		cardsList = new ArrayList<>();
		opponents = new ArrayList<>();
		try {
			backImage = new Image(new FileInputStream(path + gameInfo.getGameTheme() + "/UNO-Back.png"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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

    @Override
	public void addCards(List<Card> cards) throws RemoteException {
	    this.cardsList.addAll(cards);
		Platform.runLater(this::setMyCards);
	}

	@Override
	public Card getCard() {
		this.setMsg("It is your turn, play a card!");

        Optional<Card> pickedCard = Optional.ofNullable(getCardRec());
		Card card = pickedCard.isPresent() ? removeCard(pickedCard.get()) : null;
		return card;
	}

    private Card removeCard(Card pickedCard) {
        cardsList.remove(pickedCard);
		if (pickedCard.mySymbol == CardSymbol.WILDCARD || pickedCard.mySymbol == CardSymbol.WILDDRAWCARD) pickedCard.myColour = askColor();
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
		if (!selectedCard.canPlayOn(this.pile.peek())) {
			return getCardRec();
		}
		return selectedCard;
	}

	@Override
	public void addPile(Card card) {
		this.pile.push(card);
		try {
			image_lastcard.setImage(new Image(new FileInputStream(path + gameInfo.getGameTheme() + "/" + card.cardName)));
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

	@FXML
	private Label title;

	@FXML
	private Button btn_red, btn_green, btn_blue, btn_yellow, btn_exit, btn_send;

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

	@FXML
	private TextField chat_input;
	@FXML
	private TextArea chat_output, text_scoreboard;

	private ObservableList data = FXCollections.observableArrayList();

	private void setMyCards() {
		userBox.getChildren().clear();
		for (Card card : cardsList) {
			setASingleCardInBox(card);
		}
	}

	private void setASingleCardInBox(Card card) {
		try {
			ImageView imageView = new ImageView(
					new Image(new FileInputStream(path + gameInfo.getGameTheme() + "/" + card.cardName), 0, 125, true, true));
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

	private CardColours askColor() {
		setMsg("Chose a color!");
		btn_blue.setOpacity(1);
		btn_green.setOpacity(1);
		btn_red.setOpacity(1);
		btn_yellow.setOpacity(1);
		this.selectedColor = CardColours.BLUE;
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
			Platform.runLater(() -> info.stream()
					.filter(names -> !names.equals(userInfo.getUsername()))
					.forEach(this::addPlayer));
		}
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

	public void initialize() throws RemoteException {
		title.setText(gameInfo.getGameName());
		btn_drawCard.setImage(backImage);
		btn_drawCard.setFocusTraversable(true);

		btn_red.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			this.colourSelected = true;
			this.selectedColor = CardColours.RED;
			event.consume();
		});

		btn_blue.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			this.colourSelected = true;
			this.selectedColor = CardColours.BLUE;
			event.consume();
		});

		btn_yellow.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			this.colourSelected = true;
			this.selectedColor = CardColours.YELLOW;
			event.consume();
		});

		btn_green.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			this.colourSelected = true;
			this.selectedColor = CardColours.GREEN;
			event.consume();
		});

		btn_drawCard.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			boolDrawCard = true;
			event.consume();
		});

		btn_exit.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			closeWindow();
		});

		btn_send.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
			try {
				serverInterface.sendGameMsg(chat_input.getText(), this.gameInfo.getGameID(), this.userInfo.getToken());
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		});
	}

	private void closeWindow() {
		((Stage) btn_exit.getScene().getWindow()).close();
	}
}


