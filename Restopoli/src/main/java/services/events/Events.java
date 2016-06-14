package services.events;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;



public class Events {

    private List<Event> events = new ArrayList();
    private Map<Integer,Subscription> subscriptions = new HashMap<Integer, Subscription>();
    


    public void addEvent(Event event){
        events.add(event);
    }

    public void delEvent(String game, String type, String name, String reason, String resource, String player){
    	events.removeAll(getEvents(game, type, name, reason, resource, player)); 
    }

    public Event getEvent(String id){
        for(Event e : events){
            if(e.getId().equals(id)){
                return e;
            }
        }
        return null;
    }

    public List<Event> getEvents(String game, String type, String name, String reason, String resource, String player){
        List<Event> temp = new ArrayList();

        for(Event e : events){
            if((game == null || Pattern.matches(game, e.getGame()))
                    && (type == null || Pattern.matches(type, e.getType()))
                    && (name == null || Pattern.matches(name, e.getName()))
                    && (reason == null || Pattern.matches(reason, e.getReason()))
                    && (resource == null || Pattern.matches(resource, e.getResource()))
                    && (player == null || Pattern.matches(player, e.getPlayer()))){
                temp.add(e);
            }
        }
        return temp;
    }
    
    public void addSubscription(int id, Subscription sub){
    	subscriptions.put(id, sub);
    }
    
    public Subscription getSubscription(int id){
    	return subscriptions.get(id);
    }
    
    public List<Subscription> getSubscriptions(){
    	return new ArrayList<>(subscriptions.values());
    }
    
    
    public void delSubscription(int id){
    	subscriptions.remove(id);
    }
    
    
    
}