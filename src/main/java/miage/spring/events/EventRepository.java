package miage.spring.events;

import java.util.List;

public interface EventRepository {

    public void save(Event event);
    public void delete(Long id);

    public Event findById(Long id);
    public List<Event> findAll();
} 
