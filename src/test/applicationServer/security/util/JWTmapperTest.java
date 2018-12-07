package test.applicationServer.security.util;

import jdk.nashorn.internal.parser.JSONParser;
import main.applicationServer.security.util.JWTmapper;
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
