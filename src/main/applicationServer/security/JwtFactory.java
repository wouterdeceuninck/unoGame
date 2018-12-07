package main.applicationServer.security;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Base64;

public class JwtFactory {
    private final String filepath = "resources/keystore.jks";
    private final String SECRET = SecretValue.SECRETVALUE;
    private PrivateKey privateKey;
    private Signature signature;
    private Certificate certificate;

    public JwtFactory() {
        try {
            initKeys();
        } catch (KeyStoreException | UnrecoverableKeyException | CertificateException | NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }

    private void initKeys() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
        KeyStore keystore = KeyStore.getInstance("JKS");
        FileInputStream fileInputStream = new FileInputStream(filepath);
        keystore.load(fileInputStream, "".toCharArray());
        privateKey = (PrivateKey) keystore.getKey("signKey", "".toCharArray());
        certificate = keystore.getCertificate("signKey");
        signature = Signature.getInstance("SHA256withRSA");
    }

    public String createTailer(String header, String body) throws InvalidKeyException, SignatureException {
        signature.initSign(privateKey);
        signature.update((header + "." + body + "." + SECRET).getBytes());

        return new String(Base64.getEncoder().encode(signature.sign()));

    }

    public boolean verify(Token token) throws InvalidKeyException, SignatureException {
        signature.initVerify(certificate);
        signature.update((token.getHeader() + "." + token.getBody() + "." + SECRET).getBytes());
        return signature.verify(Base64.getDecoder().decode(token.getTailer()));
    }

}
