package databaseServer.security.util;

import databaseServer.security.Token;
import org.json.JSONException;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class JWTmapperTest {

    @Test
    public void extractInfoFromJson() throws JSONException {
        Map map = JWTmapper.getMap(createJSON());
        Assert.assertEquals(map.get("name"), "john");
        Assert.assertEquals(map.get("alg"), "SHA256");
        Assert.assertEquals(map.get("timestamp"), "1516239022");
    }

    @Test
    public void putInfoInJson() throws JSONException {
        String json = JWTmapper.getJSON(createMap());
        System.out.println(json);
        Map map = JWTmapper.getMap(json);

        Assert.assertEquals(map.get("name"), "john");
        Assert.assertEquals(map.get("alg"), "SHA256");
        Assert.assertEquals(map.get("timestamp"), "1516239022");
    }

    @Test
    public void getUsername() {
        String username = JWTmapper.getUsername(new Token("eyJhbGciOiJTSEEtMjU2In0=.eyJuYW1lIjoiMjMyZWRhMGUtY2UxYS00MmJhLTgwNTMtYTRlMTc2ZjI0NWNlIiwidGltZXN0YW1wIjoiMjAxOS0wMS0wM1QwOTowMDowMi4xMDkifQ==.Tm6LO4qqQJAsRBvT1+UUgn1EJgsdYfGHP3VTQNuAnhYACAU+Jgc3q376jDNGGZDi5NoHPypLJVMX/6n09Ewkq1gLVjelSfZKWfijYsaOIgeDI1CXC+b3iUjtrNIkYx58yNYXsnLacHezRSAlmVKyGBcP3vO/4YfN9ziIubXOXlEZfbbpXXaiJ+pv4tX0aqx4RLO29YiYp9PWShqNGRZKyzBhlxANV1OPuy3rmpsiq81J3pcna9h/X3aSVfkwHf3FghakXdpmFcRfNdGNUEoalXbz4VJPdYl/QcpHQfsjoJkj656dhiAsctJNlvp9WcKxJk/J2w8Mr+jIXTNFsw2/rQ=="));
        System.out.println(username);
    }

    private Map<String, String> createMap() {
        Map<String, String> map = new HashMap<>();
        map.put("name", "john");
        map.put("alg", "SHA256");
        map.put("timestamp", "1516239022");
        return map;
    }

    private String createJSON() {
        return "{\n" +
                "\t\"header\": {\n" +
                "\t\t\"alg\": \"SHA256\"\n" +
                "\t},\n" +
                "\t\"body\": {\n" +
                "\t\t\"name\": \"john\",\n" +
                "\t\t\"timestamp\": \"1516239022\"\n" +
                "\t}\n" +
                "}";
    }

}
