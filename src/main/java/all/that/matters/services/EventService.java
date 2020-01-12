package all.that.matters.services;

import all.that.matters.dao.EventRepository;
import all.that.matters.domain.Event;
import all.that.matters.domain.User;
import all.that.matters.dto.EventDtoOld;
import all.that.matters.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    public List<EventDtoOld> findForToday() {
        LocalDate today = LocalDate.now();
        User user = ContextUtils.getPrincipal();
        List<Event> events = eventRepository.findAllConsumedFromTodayByUserId(user.getId());

    }

    public void create(Event event) {
        eventRepository.save(event);
    }
}
