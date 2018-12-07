package main.interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.InvalidKeyException;
import java.security.SignatureException;

public interface LoginInterface extends Remote {

	/**
	 * @param username
	 * @param password
	 * @return
	 * @throws RemoteException
	 * @throws InvalidKeyException
	 * @throws SignatureException
	 */
	String getToken(String username, String password) throws RemoteException, InvalidKeyException, SignatureException;

	/**
	 * @param token
	 * @return
	 * @throws RemoteException
	 */
	boolean loginToken(String token) throws RemoteException;
}
