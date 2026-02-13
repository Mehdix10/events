package miage.spring.events;

import java.util.List;

public interface EventRepository {

    public void save(Event event);
    public void delete(Long id);
    public void update(Event event);


    public Event findById(Long id);
    public List<Event> findAll();
} 
