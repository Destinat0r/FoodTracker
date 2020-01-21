package org.training.food_tracker.services;

import org.training.food_tracker.dto.EventDTO;
import org.training.food_tracker.dto.EventDTOsPack;
import org.training.food_tracker.model.Event;
import org.training.food_tracker.model.ExceededEvent;
import org.training.food_tracker.model.Food;
import org.training.food_tracker.model.User;
import org.training.food_tracker.repo.EventRepo;
import org.training.food_tracker.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EventService {

    private EventRepo eventRepo;
    private ExceededEventService exceededEventService;

    @Autowired
    public EventService(EventRepo eventRepo, ExceededEventService exceededEventService) {
        this.eventRepo = eventRepo;
        this.exceededEventService = exceededEventService;
    }

    public List<EventDTO> findForToday() {

        List<EventDTO> eventDTOS = new ArrayList<>();

        eventRepo.findAllConsumedFromTodayByUserId(ContextUtils.getPrincipal().getId())
                .forEach(event -> eventDTOS.add(eventToEventDTO(event))
                );
        return eventDTOS;
    }

    public void create(Event event) {
        eventRepo.save(event);
    }

    public List<EventDTOsPack> getEventDTOsPacksByUserId(Long userId) {
        List<EventDTOsPack> DTOpack = packEventsToEventsDTOsPacks(findAllEventsByUserId(userId));
        DTOpack.sort((o1, o2) -> (int) (o2.getDate().toEpochDay() - o1.getDate().toEpochDay()));
        return DTOpack;
    }

    public List<EventDTOsPack> packEventsToEventsDTOsPacks(List<Event> events) {
        List<EventDTOsPack> eventDTOsPacks = new ArrayList<>();
        Set<LocalDate> days = getDaysOfEvents(events);

        Map<LocalDate, ExceededEvent> dayToExceededEventMap = getAllExceededEventsByUser(ContextUtils.getPrincipal());

        days.forEach(day -> generateEventsDTOs(events, eventDTOsPacks, dayToExceededEventMap, day));

        return eventDTOsPacks;
    }

    private Set<LocalDate> getDaysOfEvents(List<Event> events) {
        return events.stream()
                       .map(event -> event.getTimestamp().toLocalDate())
                       .collect(Collectors.toSet());
    }

    public Map<LocalDate, ExceededEvent> getAllExceededEventsByUser(User user) {
        Map<LocalDate, ExceededEvent> dayToExceededEvents = new TreeMap<>();
        List<ExceededEvent> exceededEvents = exceededEventService.findAllByUser(user);
        exceededEvents.forEach(exceededEvent -> dayToExceededEvents.put(exceededEvent.getDate(), exceededEvent));
        return dayToExceededEvents;
    }


    private void generateEventsDTOs(List<Event> events, List<EventDTOsPack> eventDTOsPacks,
            Map<LocalDate, ExceededEvent> dayToExceededEventMap, LocalDate day) {

        List<Event> eventsOfTheDay = pickEventsOfTheDay(events, day);
        List<EventDTO> eventsDTOsOfTheDay = eventsToDTOs(eventsOfTheDay);
        BigDecimal totalCalories = countTotalCalories(eventsOfTheDay);

        BigDecimal exceededCalories = getExceededCaloriesIfAny(dayToExceededEventMap, day);

        eventDTOsPacks.add(
                EventDTOsPack.builder()
                        .eventsOfTheDay(eventsDTOsOfTheDay)
                        .totalCalories(totalCalories).date(day)
                        .exceededCalories(exceededCalories)
                        .isNormExceeded(exceededCalories.intValue() != 0).build()
        );
    }

    private List<Event> pickEventsOfTheDay(List<Event> events, LocalDate day) {
        return events.stream()
                       .filter(event -> isFromDay(day, event))
                       .collect(Collectors.toList());
    }

    public List<EventDTO> eventsToDTOs(List<Event> events) {
        List<EventDTO> eventDTOs = new ArrayList<>();
        events.forEach(event -> eventDTOs.add(eventToEventDTO(event)));
        return eventDTOs;
    }

    private BigDecimal countTotalCalories(List<Event> eventsOfTheDay) {
        BigDecimal total = new BigDecimal(0);

        for (Event event : eventsOfTheDay) {
            total = total.add(event.getTotalCalories());
        }
        return total;
    }

    public BigDecimal getTotalConsumedCaloriesByUserIdAndDate(Long userId, LocalDate date) {
        return eventRepo.getTotalConsumedCaloriesByUserIdAndDate(userId, date).orElse(new BigDecimal(0));
    }

    public void createConsumeEvent(Food food, BigDecimal amount, User user) {
        Event event = Event.builder()
                              .user(user)
                              .food(food.getName())
                              .amount(amount)
                              .totalCalories(food.getCalories().multiply(amount))
                              .timestamp(LocalDateTime.now())
                              .build();
        create(event);
    }

    public List<Event> findAllEventsByUserId(Long id) {
        return eventRepo.findAllByUserId(id);
    }

    public Map<LocalDate, List<EventDTO>> getDayToEventDTOsMapByUserId(Long userId) {
        return mapEventDTOsToDay(findAllEventsByUserId(userId));
    }

    private EventDTO eventToEventDTO(Event event) {
        return EventDTO.builder()
                        .foodName(event.getFood())
                        .foodAmount(event.getAmount())
                        .totalCalories(event.getTotalCalories())
                        .timestamp(event.getTimestamp()).build();
    }

    private BigDecimal getExceededCaloriesIfAny(Map<LocalDate, ExceededEvent> dayToExceededEventMap, LocalDate day) {
        return dayToExceededEventMap.containsKey(day)
                                              ? dayToExceededEventMap.get(day).getExcessive_calories()
                                              : new BigDecimal(0);
    }

    private Map<LocalDate, List<EventDTO>> mapEventDTOsToDay(List<Event> events) {
        Set<LocalDate> days = getDaysOfEvents(events);

        Map<LocalDate, List<EventDTO>> dateToEventDTOs = new TreeMap<>();

        days.forEach(day -> {
            dateToEventDTOs.put(day, new ArrayList<EventDTO>());
            sortEventsByDay(events, dateToEventDTOs, day);
        });

        return dateToEventDTOs;
    }

    private void sortEventsByDay(List<Event> events, Map<LocalDate, List<EventDTO>> dateToEvents, LocalDate day) {
        events.stream()
                .filter(event -> isFromDay(day, event))
                .forEach(event ->
                                 dateToEvents.get(day).add(eventToEventDTO(event)));
    }

    private boolean isFromDay(LocalDate day, Event event) {
        return event.getTimestamp().toLocalDate().equals(day);
    }
}
