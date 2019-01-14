package databaseServer.security;

import databaseServer.security.util.JWTmapper;
import databaseServer.security.util.SecretValue;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

public class JWTVerifier {
    private final String filepath = "resources/keystore.jks";
    private final String SECRET = SecretValue.SECRETVALUE;
    private PrivateKey privateKey;
    private Signature signature;
    private Certificate certificate;

    public JWTVerifier() {
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

    String createTailer(String header, String body) throws InvalidKeyException, SignatureException {
        signature.initSign(privateKey);
        signature.update((header + "." + body + "." + SECRET).getBytes());

        return new String(Base64.getEncoder().encode(signature.sign()));

    }

    public boolean verify(Token token) {
        try {
            signature.initVerify(certificate);
            signature.update((token.getHeader() + "." + token.getBody() + "." + SECRET).getBytes());
            String timestamp = JWTmapper.getMap(token.getLiteralString()).get("timestamp");
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(timestamp);
            boolean needsRefresh = !date.after(getCurrentDateMinusOneDay());
            return signature.verify(Base64.getDecoder().decode(token.getTailer())) && !needsRefresh;
        } catch (SignatureException | InvalidKeyException | ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Date getCurrentDateMinusOneDay() {
        LocalDateTime localDateTime = LocalDateTime.ofInstant(new Date().toInstant(), ZoneId.systemDefault()).minusDays(1);
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

}
