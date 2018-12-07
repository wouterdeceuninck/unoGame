package main.applicationServer.security;

import main.applicationServer.security.util.JWTmapper;

import java.security.InvalidKeyException;
import java.security.SignatureException;
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
        return (header
                + "."
                + body
                + "."
                + tailer);
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

        public Token build(JwtFactory jwtFactory) {
            try {
                return new Token(JWTmapper.getHeader(map)
                        ,JWTmapper.getBody(map)
                        ,jwtFactory.createTailer(JWTmapper.getHeader(map), JWTmapper.getBody(map)));

            } catch (InvalidKeyException | SignatureException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
