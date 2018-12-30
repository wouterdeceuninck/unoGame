package databaseServer.security;

import databaseServer.businessObjects.UserObject;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordVerifier {

    public static boolean verifyPassword(String password, UserObject user) {
        String[] hashAndSalt = user.getPassword().split("_");
        String hash = hashAndSalt[0];
        String salt = hashAndSalt[1];
        return BCrypt.hashpw(password, salt).equals(hash);
    }

    public static String createPassword(String password) {
        String gensalt = BCrypt.gensalt();
        return BCrypt.hashpw(password, gensalt) + "_" + gensalt;
    }
}
