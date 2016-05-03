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
        List<Integer> delIndexes = new ArrayList<Integer>();
        for(Event e : events){
            if((game == null || Pattern.matches(e.getGame(), game))
                    && (type == null || Pattern.matches(e.getType(), type))
                    && (name == null || Pattern.matches(e.getName(), name))
                    && (reason == null || Pattern.matches(e.getReason(), reason))
                    && (resource == null || Pattern.matches(e.getResource(), resource))
                    && (player == null || Pattern.matches(e.getPlayer(), player))){
                delIndexes.add(events.indexOf(e));
            }
        }
        for (Integer i : delIndexes){
            events.remove(i);
        }
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
            if((game == null || Pattern.matches(e.getGame(), game))
                    && (type == null || Pattern.matches(e.getType(), type))
                    && (name == null || Pattern.matches(e.getName(), name))
                    && (reason == null || Pattern.matches(e.getReason(), reason))
                    && (resource == null || Pattern.matches(e.getResource(), resource))
                    && (player == null || Pattern.matches(e.getPlayer(), player))){
                temp.add(e);
            }
        }
        return temp;
    }
}