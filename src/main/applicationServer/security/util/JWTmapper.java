package main.applicationServer.security.util;

import org.json.*;

import java.util.HashMap;
import java.util.Map;

public class JWTmapper {

    public static Map<String, String> getMap(String json) {
        JSONObject jsonObject = null;
        Map<String, String> map = new HashMap<>();

        try {
            jsonObject = new JSONObject(json);
            map.put("alg", jsonObject.getJSONObject("header").getString("alg"));
            map.put("name", jsonObject.getJSONObject("body").getString("name"));
            map.put("timestamp", jsonObject.getJSONObject("body").getString("timestamp"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return map;
    }

    public static String getJSON(Map<String, String> map) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("header", (new JSONObject(getHeader(map))));
            jsonObject.put("body", (new JSONObject(getBody(map))));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    public static String getHeader(Map<String, String> map)  {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("alg", map.get("alg"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }

    public static String getBody(Map<String, String> map ) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name", map.get("name"))
                    .put("timestamp", map.get("timestamp"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject.toString();
    }
}
