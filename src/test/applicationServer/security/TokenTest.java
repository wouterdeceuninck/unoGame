package applicationServer.security;

import databaseServer.security.JwtFactory;
import databaseServer.security.Token;
import org.junit.Assert;
import org.junit.Test;

import java.security.InvalidKeyException;
import java.security.SignatureException;

public class TokenTest {
    private JwtFactory jwtFactory = new JwtFactory();

    @Test
    public void createTokenFromBuilder() {
        Token token = getToken("12-12-2018 12:34:25.256");
        System.out.println(token.toString());
    }

    @Test
    public void verifyToken() {
        Assert.assertFalse(jwtFactory.verify(getToken("12-12-2018 12:34:25.256")));
    }

    private Token getToken(String timestamp) {
        return new Token.Builder().setAlg("sha256").setName("name").setTimestamp(timestamp).build();
    }

}
