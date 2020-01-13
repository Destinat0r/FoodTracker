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
}
