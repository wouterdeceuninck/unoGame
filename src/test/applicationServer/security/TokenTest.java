package test.applicationServer.security;

import main.applicationServer.security.JwtFactory;
import main.applicationServer.security.Token;
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
        try {
            jwtFactory.verify(token);
            Assert.assertTrue(jwtFactory.verify(token));
        } catch (InvalidKeyException | SignatureException e) {
            e.printStackTrace();
        }
    }

}
