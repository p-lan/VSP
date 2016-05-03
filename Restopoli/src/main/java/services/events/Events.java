package services.events;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Events {

    private List<Event> events = new ArrayList();


    public void addEvent(Event event){
        events.add(event);
    }

    public void delEvent(String game, String type, String name, String reason, String resource, String player){
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
    	events.removeAll(temp); 
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
}