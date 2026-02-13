package miage.spring.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Repository;

@Repository
public class EventRepositoryInMemory implements EventRepository {

    private final Map<Long,Event> events = new TreeMap<>();
    private static Long idCounter = 1L;

    @Override
    public void save(Event event) {
        if(event.getId() == null)
            event.setId(idCounter++);
        events.put(event.getId(), event);
    }

    @Override
    public void delete(Long id) {
        this.events.remove(id);
    }

    @Override
    public Event findById(Long id) {
        return this.events.get(id);
    }

    @Override
    public List<Event> findAll() {
        return new ArrayList<Event>(this.events.values());
    }

}
