package services.events;

import java.util.ArrayList;
import java.util.List;

public class Events {

    private List<Event> events = new ArrayList();


    public void addEvent(Event event){
        events.add(event);
    }

    public void delEvent(String game, String type, String name, String reason, String resource, String player){
        List<Integer> delIndexes = new ArrayList<Integer>();
        for(Event e : events){
            if(e.getGame().matches("(https://141\\.22\\.34\\.15/cnt/\\d+\\.\\d+\\.\\d+\\.\\d+/\\d\\d\\d\\d/(\\w)+(/\\w)*)|()")
                    && e.getType().matches("([a-zA-Z_]+)|()") && e.getName().matches("(lmnp_[a-zA-Z_0-9]+)|()") && e.getReason().matches("([a-zA-Z _0-9]+)|()")
                    && e.getResource().matches("(https://141\\.22\\.34\\.15/cnt/\\d+\\.\\d+\\.\\d+\\.\\d+/\\d\\d\\d\\d/(\\w)+(/\\w)*)|()")
                    && e.getPlayer().matches("(https://141\\.22\\.34\\.15/cnt/\\d+\\.\\d+\\.\\d+\\.\\d+/\\d\\d\\d\\d/(\\w)+(/\\w)*)|()")){
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
            if(e.getGame().matches("(https://141\\.22\\.34\\.15/cnt/\\d+\\.\\d+\\.\\d+\\.\\d+/\\d\\d\\d\\d/(\\w)+(/\\w)*)|()")
                    && e.getType().matches("([a-zA-Z_]+)|()") && e.getName().matches("(lmnp_[a-zA-Z_0-9]+)|()") && e.getReason().matches("([a-zA-Z _0-9]+)|()")
                    && e.getResource().matches("(https://141\\.22\\.34\\.15/cnt/\\d+\\.\\d+\\.\\d+\\.\\d+/\\d\\d\\d\\d/(\\w)+(/\\w)*)|()")
                    && e.getPlayer().matches("(https://141\\.22\\.34\\.15/cnt/\\d+\\.\\d+\\.\\d+\\.\\d+/\\d\\d\\d\\d/(\\w)+(/\\w)*)|()")){
                temp.add(e);
            }
        }
        return temp;
    }
}