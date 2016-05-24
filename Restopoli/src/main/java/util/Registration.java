package util;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by norman gersdorf on 03.05.16.
 */
public class Registration {

    private static final String YELLOW_PAGES = "http://172.18.0.5:4567/services";
    private static final String YELLOW_PAGES_NAME = "http://172.18.0.5:4567/services/of/name/";
    
    
    

    public static String registriereService(String name, String description, String service, String uri){
        
    	List<String> idsPerService = new ArrayList();
    	List<String> idsPerName = new ArrayList();
    	String fullUri = "";
    	InetAddress ip;
    	String port="";
    	
    	idsPerName = getExistingServiceIdsPerName(name);
    	
    	if(!idsPerName.isEmpty()){
    		 idsPerService = getExistingServicePerID(idsPerName,service);
        	
        	if(!idsPerService.isEmpty()){        		
        		delService(idsPerService);	
        	}
    	}

    	try {
    		ip = InetAddress.getLocalHost();
    		fullUri= "http://"+ip.getHostAddress()+":4567";
    		HttpResponse<String> res = Unirest.post(YELLOW_PAGES)
	                .header("Content-Type", "application/json")
	                .body(createBody(name, description, service, fullUri))
	                .asString();
    	} catch (UnknownHostException | UnirestException e) {
        	e.printStackTrace();
    	}
        return null;
    }
    
     
    
    
    
    private static void delService(List<String> idsPerService) {
    	
    	
    	String serviceToDel="";
    	
	    for(String elem : idsPerService){
			serviceToDel = YELLOW_PAGES+"/"+elem;
			
			System.out.println("Service:del: "+ elem);
			
			try {
				Unirest.delete(serviceToDel).asString();
			} catch (UnirestException e) {
				e.printStackTrace();
			}
		}
	}

	private static JSONObject createBody(String name, String description, String service, String uri) {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("description", description);
        json.put("service", service);
        json.put("uri", uri);
        return json;
    }
    
    
    private static List<String> getExistingServiceIdsPerName(String name){
    	String toSearchName = YELLOW_PAGES_NAME+name;
    	JSONArray antwort = new JSONArray();
    	JSONObject antwortElem;
    	String[] temp;
    	List<String> ids = new ArrayList<>();
    	
    	//fragt alle Services ab die den angegebenen namen haben
    	try {
    		antwort = Unirest.get(toSearchName).asJson().getBody().getArray();
            
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    	System.out.println("Test get List of Services");
    	
    	
    	//durch läuft die Antwort um sie in eine Liste zu speichern 
    	// und ueberflüssige zeichen zu löschen
    	
    	 for(Object jsonEntry : antwort) {
    		 antwortElem = (JSONObject)jsonEntry;
    		 
    		 temp = antwortElem.get("services").toString().split(",");
 			
    		 for(int i = 0; i<temp.length;i++){
    			 if(!temp[i].replaceAll("[^\\d]", "").isEmpty()){
    				 ids.add(temp[i].replaceAll("[^\\d]", ""));
    				 System.out.println("Service:name: " + temp[i]);
    			 }
    		 }
 		 }
		return ids;
    }
    
	private static List<String> getExistingServicePerID(List<String> ids,String toFindService){
    	
    	//fragt alle ids ab und speichert sie
    	List<String> temp = new ArrayList();
    	String serviceToGet="";
    	
    	String antwort="";
    	
    	try {
    	
	    	for(String elem : ids){
	    		
	    		serviceToGet= YELLOW_PAGES+"/"+elem;
	    		System.out.println("Service:Service: "+ serviceToGet);
	    		antwort = Unirest.get(serviceToGet).asJson().getBody().getObject().get("service").toString();
	    		System.out.println("Service:Service: "+ antwort);
	    		
	    		if(antwort.equals(toFindService)){
	    			temp.add(elem);
	    		}
	    	}
            
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    	
    	return temp;
    }
    
    
   
    
    
    
}
