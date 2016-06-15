package util;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class GetServiceURI {
	
	 private static final String YELLOW_PAGES = "http://172.18.0.5:4567/services";
	 private static final String YELLOW_PAGES_NAME = "http://172.18.0.5:4567/services/of/name/";
	 
	 private String serviceName;

	public GetServiceURI(String serviceName) {
		this.serviceName = serviceName;
	}
	
	public String getUriOfTheService(){
		List<String> serviceIDS= getServiceIDperName();
		return getUriOftheServicePerID(serviceIDS);
	}
	
	
	private List<String> getServiceIDperName(){
		String toSearchName = YELLOW_PAGES_NAME+serviceName;
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
    	//return die erste id
		return ids;
	}
	
	private String getUriOftheServicePerID(List<String> ids){
		
		//fragt alle ids ab und speichert sie
    	List<String> temp = new ArrayList();
    	String serviceToGet="";
    	
    	JSONObject antwort;
    	String uri="";
    	try {
    	
	    	for(String elem : ids){
	    		
	    		serviceToGet= YELLOW_PAGES+"/"+elem;
	    		//holt das object
	    		antwort = Unirest.get(serviceToGet).asJson().getBody().getObject();
	    		
	    		uri = antwort.get("uri").toString()+""+antwort.get("service").toString();
	    			
	    		
	    	}
            
        } catch (UnirestException e) {
            e.printStackTrace();
        }
    	//gibt die erste uri wieder die er gefunden hat
    	return uri;
	}

}
