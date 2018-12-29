package main.databaseServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import main.applicationServer.uno.Card;

public class Database {

	private Connection connection;
	private Statement statement;

	public Database(String uri) throws SQLException{
		File dbName = new File(uri);
		if (dbName.exists()) {
			connection = DriverManager.getConnection("jdbc:sqlite:" + uri);
			System.out.println("UNO database opened successfully! " + " [ " + uri + " ]");
		} else {
			try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + uri)) {
				if (conn != null) {

					System.out.println("UNO database has been created." + "[ " + uri + " ]");
				}
			} catch (SQLException e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public void createUserTable() throws SQLException {

		System.out.println("UserObject Table created successfully");
	}


	public void createGameTable() throws SQLException {
		try {
			String sql = "CREATE TABLE IF NOT EXISTS Game (\n"
					+ "	game_id     	INTEGER     PRIMARY KEY   AUTOINCREMENT,\n"
					+ "	game_name       VARCHAR     NOT NULL,\n" + " players       	INTEGER     NOT NULL,\n"
					+ " active       	BOOLEAN     NOT NULL, \n" + " serverport		INTEGER		NOT NULL, \n"
					+ " game_theme		INTEGER, \n" + "FOREIGN KEY(game_id) REFERENCES Game(game_id)\n" + ");";

			statement = connection.createStatement();
			statement.execute(sql);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		System.out.println("GameObject table created successfully");
	}

//	public void createImagesTable() throws SQLException {
//		try {
//
//			String sql = "CREATE TABLE IF NOT EXISTS Images (\n"
//					+ "	card_id         INTEGER     PRIMARY KEY     AUTOINCREMENT,\n"
//					+ "	card_color      INTEGER     NOT NULL,\n" + "	card_value      INTEGER     NOT NULL,\n"
//					+ " card_theme		INTEGER,\n" + " card_image      BLOB        NOT NULL\n" + ");";
//
//			connection = DriverManager.getConnection("jdbc:sqlite:" + URI);
//			statement = connection.createStatement();
//			statement.execute(sql);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.exit(0);
//		}
//		System.out.println("Images table created successfully");
//	}

	private void saveToken(String username, String signedToken, Timestamp timestamp) {

		String sql = "UPDATE Users SET token = ?, timestamp =? WHERE username = ?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, signedToken);
			pstmt.setTimestamp(2, timestamp);
			pstmt.setString(3, username);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("insert user completed!");

	}

	public String getAllUsers() {
		String sql = "SELECT user_id, username, password FROM USERS";
		StringBuffer sb = new StringBuffer();
		try {
			statement = connection.createStatement();
			ResultSet rs = statement.executeQuery(sql);
			while (rs.next()) {
				sb.append(rs.getString("USER_ID") + "\t");
				sb.append(rs.getString("USERNAME") + "\t");
				sb.append(rs.getString("PASSWORD") + "\n");
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return sb.toString();
	}

	public boolean loginUser(String username, String password)  {
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		String sql = "SELECT PASSWORD FROM USERS WHERE USERNAME = ?";

		try {
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();

			String hashed_pw = rs.getString("password");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return true;
	}

	public void addUserToGame(int game_id, String user1, String user2, String user3, String user4) {
		String sql = "INSERT INTO GameToUSer(game_id, user1, user2, user3, user4) VALUES(?,?,?,?,?)";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, game_id);
			pstmt.setString(2, user1);
			pstmt.setString(3, user2);
			pstmt.setString(4, user3);
			pstmt.setString(5, user4);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("New gameToUser added!");
	}

	public void addGame(String dbID, String name, int aantalSpelers, int serverport, int theme) {
		String sql = "INSERT INTO Game(game_id, game_name, players, active ,serverport, game_theme) VALUES(?,?,?,?,?,?)";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, dbID);
			pstmt.setString(2, name);
			pstmt.setInt(3, aantalSpelers);
			pstmt.setBoolean(4, false);
			pstmt.setInt(5, serverport);
			pstmt.setInt(6, theme);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("New game added!");
	}


	public List<String> getActiveGames() throws SQLException {
		String sql = "SELECT game_id, game_name, players, serverport, game_theme FROM Game WHERE active= 1";

		PreparedStatement pstmt = connection.prepareStatement(sql);
		ResultSet rs = pstmt.executeQuery();
		List<String> games = new ArrayList<>();
		while (rs.next()) {
			StringBuilder sb = new StringBuilder();
			sb.append(rs.getString("game_id"));
			sb.append("\t");
			sb.append(rs.getString("game_name"));
			sb.append("\t");
			sb.append(rs.getInt("players"));
			sb.append("\t");
			sb.append(rs.getInt("serverPort"));
			sb.append("\t");
			sb.append(rs.getInt("game_theme"));
			sb.append("\t");
			games.add(sb.toString());
		}
		return games;
	}

	public void StopGame(int game_id) {
		String sql = "UPDATE Game SET active = 0 WHERE game_id = ?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, game_id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		System.out.println("Stopped GameObject");

	}
}
