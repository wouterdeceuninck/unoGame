package main.databaseServer;

import java.io.IOException;
import java.rmi.AlreadyBoundException;
import java.security.InvalidKeyException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.SQLException;

import main.dispatcher.dispatcherInterfaceImpl;

public class MainTest {

	public static void main(String[] args)
			throws UnrecoverableKeyException, KeyStoreException, NoSuchAlgorithmException, CertificateException,
			SQLException, IOException, InvalidKeyException, SignatureException, AlreadyBoundException {
		String uri = "D:\\Google Drive\\School\\2017-2018\\1e Semester\\Gedistribueerde Systemen\\Opdracht UNO\\GIT_UNO\\main.uno";

		dispatcherInterfaceImpl dispatcherInterfaceImpl = new dispatcherInterfaceImpl();

		System.out.println("einde");
	}
}