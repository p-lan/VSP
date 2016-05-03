package util;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;

/**
 * Created by patricklanger on 12.04.16.
 */
public class Registration {

    private static final String YELLOW_PAGES = "http://172.18.0.5:4567/services";

    public static String registriereService(String name, String description, String service, String uri){
        try {
            HttpResponse<String> res = Unirest.post(YELLOW_PAGES)
                    .header("Content-Type", "application/json")
                    .body(createBody(name, description, service, uri))
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }

        return null;
    }

    private static JSONObject createBody(String name, String description, String service, String uri) {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("description", description);
        json.put("service", service);
        json.put("uri", uri);
        return json;
    }
}
