package util;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by norman gersdorf on 03.05.16.
 */
public class Registration {

    private static final String YELLOW_PAGES = "http://172.18.0.5:4567/services";
    private static final String YELLOW_PAGES_NAME = "http://172.18.0.5:4567/services/of/name/";
    
    
    

    public static String registriereService(String name, String description, String service, String uri){
        
    	
    	
    	
    	
    	
    	JSONArray test = serviceExistiert(name);
    	
    	if(test != null){
    		delOldService(test);
    	}
    	
    	
    	
    	/*try {
            HttpResponse<String> res = Unirest.post(YELLOW_PAGES)
                    .header("Content-Type", "application/json")
                    .body(createBody(name, description, service, uri))
                    .asString();
        } catch (UnirestException e) {
            e.printStackTrace();
        }*/

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
    
    
    private static JSONArray serviceExistiert(String name){
    	String toSearchName = YELLOW_PAGES_NAME+"group_42";
    	JSONArray res = null;
    	try {
            res = Unirest.get(toSearchName).asJson().getBody().getArray();
            
            System.out.println("Test get List of Services");
            
            
            
        } catch (UnirestException e) {
            e.printStackTrace();
        }
		return res;
    }
    
    private static void delOldService(JSONArray zuloeschen){
    	
    	JSONObject elem;
    	String [] ids;
    	
    	 for(Object jsonEntry : zuloeschen) {
    		 elem = (JSONObject)jsonEntry;
    		 
    		 ids = elem.get("services").toString().split(",");
 			
    		 for(int i = 0; i<ids.length;i++){
    			 System.out.println("service: "+ids[i].replaceAll("[^\\d]", ""));
    		 }
	 
 		}
	
    }
    
    
    
}
