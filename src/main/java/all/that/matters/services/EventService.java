package all.that.matters.services;

import all.that.matters.dao.EventRepository;
import all.that.matters.domain.Event;
import all.that.matters.dto.EventDto;
import all.that.matters.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventService {

    private EventRepository eventRepository;

    @Autowired
    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    public List<EventDto> findForToday() {

        List<EventDto> eventDtos = new ArrayList<>();

        eventRepository.findAllConsumedFromTodayByUserId(ContextUtils.getPrincipal().getId())
                .forEach(event -> eventDtos.add(
                            EventDto.builder()
                                    .foodName(event.getFood().getName())
                                    .foodAmount(event.getAmount())
                                    .totalCalories(event.getTotalCalories())
                                    .timestamp(event.getTimestamp())
                                    .build())
                );
        return eventDtos;
    }

    public Double getUserDailyNorm() {
        return ContextUtils.getPrincipal().getBiometrics().getDailyNorm();
    }

    public Double getConsumedCaloriesForToday() {
        return findForToday().stream().mapToDouble(EventDto::getTotalCalories).sum();
    }

    public boolean isDailyNormExceeded() {
        return getUserDailyNorm() < getConsumedCaloriesForToday();
    }

    public Double getExceededCalories() {
        return isDailyNormExceeded() ? getConsumedCaloriesForToday() - getUserDailyNorm() : 0.0;
    }

    public void create(Event event) {
        eventRepository.save(event);
    }
}
