package main.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import main.applicationServer.uno.Card;

public interface clientInterface extends Remote {

	/**
	 * @param s
	 * @throws RemoteException
	 */
	public void tell(String s) throws RemoteException;

	/**
	 * @throws RemoteException
	 */
	public void askColour() throws RemoteException;

	/**
	 * @param server
	 * @throws RemoteException
	 */
	public void giveInterface(dispatcherInterface server) throws RemoteException;

	/**
	 * @param card
	 * @throws RemoteException
	 */
	public void updatePile(Card card) throws RemoteException;

	/**
	 * @param cards
	 * @throws RemoteException
	 */
	public void drawCards(List<Card> cards) throws RemoteException;

	/**
	 * @param server
	 * @throws RemoteException
	 */
	public void giveInterface(ServerInterface server) throws RemoteException;

	/**
	 * @param cards
	 * @throws RemoteException
	 */
	public void giveCards(List<Card> cards) throws RemoteException;

	/**
	 * @throws RemoteException
	 */
	public void getCard() throws RemoteException;

	/**
	 * @return
	 * @throws RemoteException
	 */
	public List<Card> getCards() throws RemoteException;

	/**
	 * @param card
	 * @throws RemoteException
	 */
	public void updateTopCard(Card card) throws RemoteException;

}
