package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import applicationServer.uno.cards.Card;
import applicationServer.uno.cards.properties.CardColours;

public interface gameControllerInterface extends Remote {
	public void setMsg(String msg) throws RemoteException;
	public void setScoreboard(List<String> scoreboard) throws RemoteException;
	public void addPile(Card card) throws RemoteException;
	public void addCards(List<Card> cards) throws RemoteException;
	public Card getCard() throws RemoteException;
	public void setCardAmountPlayer(String username, int amount) throws RemoteException;
	public void sendPlayerInfo(ArrayList<String> info) throws RemoteException;
}
