package all.that.matters.services;

import all.that.matters.dto.EventDto;
import all.that.matters.model.Event;
import all.that.matters.model.Food;
import all.that.matters.model.User;
import all.that.matters.repo.EventRepo;
import all.that.matters.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventService {

    private EventRepo eventRepo;

    @Autowired
    public EventService(EventRepo eventRepo) {
        this.eventRepo = eventRepo;
    }

    public List<EventDto> findForToday() {

        List<EventDto> eventDtos = new ArrayList<>();

        eventRepo.findAllConsumedFromTodayByUserId(ContextUtils.getPrincipal().getId())
                .forEach(event -> eventDtos.add(eventToEventDto(event))
                );
        return eventDtos;
    }

    public void create(Event event) {
        eventRepo.save(event);
    }

    public BigDecimal getTotalConsumedCaloriesByUserIdAndDate(Long userId, LocalDate date) {
        return eventRepo.getTotalConsumedCaloriesByUserIdAndDate(userId, date).orElse(new BigDecimal(0.0));
    }

    public void createConsumeEvent(Food food, BigDecimal amount, User user) {
        Event event = Event.builder()
                              .user(user)
                              .food(food)
                              .amount(amount)
                              .totalCalories(food.getCalories().multiply(amount))
                              .timestamp(LocalDateTime.now())
                              .build();
        create(event);
    }

    public List<Event> findAllByUserId(Long id) {
        return eventRepo.findAllByUserId(id);
    }

    public Map<LocalDate, List<EventDto>> getDayToEventDtosMapByUserId(Long userId) {
        return mapEventDtosToDay(findAllByUserId(userId));
    }

    public List<EventDto> eventsToDtos(List<Event> events) {
        List<EventDto> eventDtos = new ArrayList<>();
        events.forEach(event -> eventDtos.add(eventToEventDto(event)));
        return eventDtos;
    }

    private EventDto eventToEventDto(Event event) {
        return EventDto.builder()
                        .foodName(event.getFood().getName())
                        .foodAmount(event.getAmount())
                        .totalCalories(event.getTotalCalories())
                        .timestamp(event.getTimestamp()).build();
    }

    private Map<LocalDate, List<EventDto>> mapEventDtosToDay(List<Event> events) {
        List<LocalDate> days = events.stream()
                                       .map(event -> event.getTimestamp().toLocalDate())
                                       .collect(Collectors.toList());

        Map<LocalDate, List<EventDto>> dateToEventDtos = new TreeMap<>();

        days.forEach(day -> {
            dateToEventDtos.put(day, new ArrayList<EventDto>());
            sortEventsByDay(events, dateToEventDtos, day);
        });

        return dateToEventDtos;
    }

    private void sortEventsByDay(List<Event> events, Map<LocalDate, List<EventDto>> dateToEvents, LocalDate day) {
        events.stream()
                .filter(event -> isFromDay(day, event))
                .forEach(event ->
                                 dateToEvents.get(day).add(eventToEventDto(event)));
    }

    private boolean isFromDay(LocalDate day, Event event) {
        return event.getTimestamp().toLocalDate().equals(day);
    }
}
