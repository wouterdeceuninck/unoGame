package applicationServer.security;

import databaseServer.security.JWTVerifier;
import databaseServer.security.Token;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {
    private JWTVerifier JWTVerifier = new JWTVerifier();

    @Test
    public void createTokenFromBuilder() {
        Token token = getToken("12-12-2018 12:34:25.256");
        System.out.println(token.toString());
    }

    @Test
    public void verifyToken() {
        Assert.assertFalse(JWTVerifier.verify(getToken("12-12-2018 12:34:25.256")));
    }

    private Token getToken(String timestamp) {
        return new Token.Builder().setAlg("sha256").setName("name").setTimestamp(timestamp).build();
    }

}
