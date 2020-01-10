package all.that.matters.services;

import all.that.matters.dao.EventRepository;
import all.that.matters.domain.Event;
import all.that.matters.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {

    private EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<Event> findForToday() {
        LocalDate today = LocalDate.now();
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return eventRepository.findAllConsumedFromTodayByUserId(user.getId());
    }

    public void create(Event event) {
        eventRepository.save(event);
    }
}
