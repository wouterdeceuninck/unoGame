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
        Token token = new Token.Builder().setAlg("sha256").setName("name").setTimestamp("12-12-12").build();
        System.out.println(token.toString());
    }

    @Test
    public void verifyToken() {
        Token token = new Token.Builder().setAlg("sha256").setName("name").setTimestamp("12-12-12").build();
        Assert.assertTrue(jwtFactory.verify(token));
        Assert.assertTrue(jwtFactory.verify(new Token(token.toString())));
    }

}
