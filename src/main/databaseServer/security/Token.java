package databaseServer.security;

import databaseServer.security.util.JWTmapper;

import java.security.InvalidKeyException;
import java.security.SignatureException;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class Token {
    private final String header;
    private final String body;
    private final String tailer;

    public Token(String header, String body, String tailer) {
        this.header = header;
        this.body = body;
        this.tailer = tailer;
    }

    public Token(String token) {
        String[] split = token.split("\\.");
        this.header = new String(Base64.getDecoder().decode(split[0]));
        this.body = new String(Base64.getDecoder().decode(split[1]));
        this.tailer = split[2];
    }

    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public String getTailer() {
        return tailer;
    }

    public byte[] getBytes() {
        return this.toString().getBytes();
    }

    @Override
    public String toString() {
        return (Base64.getEncoder().encodeToString(header.getBytes()) + "."
                + Base64.getEncoder().encodeToString(body.getBytes())  + "."
                + tailer);
    }

    public String getLiteralString() {
        return "{\n" +
                "\t\"header\": " +
                header +
                ",\n" +
                "\t\"body\": " +
                body +
                "\n" +
                "}";
    }

    public static class Builder {
        private Map<String, String> map = new HashMap<>();

        public Builder setAlg(String alg) {
            map.put("alg", alg);
            return this;
        }

        public Builder setTimestamp(String timestamp) {
            map.put("timestamp", timestamp);
            return this;
        }

        public Builder setName(String name) {
            map.put("name", name);
            return this;
        }

        public Token build() {
            try {
                return new Token(JWTmapper.getHeader(map)
                        , JWTmapper.getBody(map)
                        , new JwtFactory().createTailer(JWTmapper.getHeader(map), JWTmapper.getBody(map)));

            } catch (InvalidKeyException | SignatureException e) {
                e.printStackTrace();
                return null;
            }
        }
    }
}
