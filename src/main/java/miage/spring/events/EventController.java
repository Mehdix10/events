package miage.spring.events;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/events")
public class EventController {
    
    private final EventRepository eventRepository;

    public EventController(EventRepository eventRepository)
    {
        this.eventRepository = eventRepository;
    }

    @GetMapping
    public String listEvents(Model model)
    {
        final var t = eventRepository.findAll();
        System.out.println(t);
        model.addAttribute("events", t);
        return "events/list";
    }

    @GetMapping("/new")
    public String newEventForm(Model model)
    {
        model.addAttribute("event", new Event());
        return "events/new";
    }
    @PostMapping("/new")
    public String saveEvent(@ModelAttribute Event event)
    {
        this.eventRepository.save(event);
        return "redirect:/events";
    }
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable long id, Model model)
    {
        final Event e= this.eventRepository.findById(id);
        model.addAttribute("event", e);
        return "events/new";
    }
    @PostMapping("/delete/{id}")
    public String deleteEvent(@PathVariable long id)
    {
        this.eventRepository.delete(id);
        return "redirect:/events";
    }
}
