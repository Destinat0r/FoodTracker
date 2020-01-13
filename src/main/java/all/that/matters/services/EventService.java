package all.that.matters.services;

import all.that.matters.dao.EventRepository;
import all.that.matters.domain.Event;
import all.that.matters.domain.Food;
import all.that.matters.domain.User;
import all.that.matters.dto.EventDto;
import all.that.matters.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
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

    public void create(Event event) {
        eventRepository.save(event);
    }

    public Double getTotalConsumedCaloriesByUserIdAndDate(Long userId, LocalDate date) {
        return eventRepository.getTotalConsumedCaloriesByUserIdAndDate(userId, date).orElse(0.0);
    }

    public void createConsumeEvent(Food food, Double amount, User user) {
        Event event = Event.builder()
                              .user(user)
                              .food(food)
                              .amount(amount)
                              .totalCalories(food.getCalories() * amount)
                              .timestamp(LocalDateTime.now())
                              .build();
        create(event);
    }
}
