package org.training.food_tracker.services.defaults;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.training.food_tracker.model.ConsumedFood;
import org.training.food_tracker.model.Day;
import org.training.food_tracker.model.User;
import org.training.food_tracker.repo.DayRepo;
import org.training.food_tracker.services.DayService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Log4j2

@Service
public class DayServiceDefault implements DayService {

    private DayRepo dayRepo;

    @Autowired
    public DayServiceDefault(DayRepo dayRepo) {
        this.dayRepo = dayRepo;
    }

    public Day getCurrentDayOfUser(User user) {

        Optional<Day> optionalDay = dayRepo.findByUserAndDate(user, LocalDate.now());
        if (optionalDay.isPresent()) {
            Day day = optionalDay.get();
            sortConsumedFoodByTimeDesc(day.getConsumedFoods());
            return day;
        }
        Day newDay = Day.builder()
                         .date(LocalDate.now())
                         .consumedFoods(new ArrayList<>())
                         .caloriesConsumed(new BigDecimal(0))
                         .user(user)
                         .build();
        dayRepo.save(newDay);
        return newDay;
    }

    private void sortConsumedFoodByTimeDesc(List<ConsumedFood> foods) {
        foods.sort((food1, food2) -> (food2.getTime().toSecondOfDay() - food1.getTime().toSecondOfDay()));
    }

    @Override public void updateDay(Day day, ConsumedFood consumedFood) {

            log.debug("updateDay() :: adding calories ");
            addConsumedCalories(day, consumedFood.getTotalCalories());

            log.debug("updateDay() :: calories of the day after update {}", day.getCaloriesConsumed());
            setExceededCaloriesIfAny(day);
            dayRepo.update(day);
    }

    private void addConsumedCalories(Day day, BigDecimal calories) {
        day.setCaloriesConsumed(day.getCaloriesConsumed().add(calories));
    }

    private void setExceededCaloriesIfAny(Day day) {
        BigDecimal exceededCalories = getExceededCalories(day);
        if (exceededCalories.intValue() > 0) {
            day.setDailyNormExceeded(true);
            day.setExceededCalories(exceededCalories);
        }
    }

    private BigDecimal getExceededCalories(Day day) {
        return day.getCaloriesConsumed().subtract(day.getUser().getDailyNormCalories());
    }

    public List<Day> getAllDaysByUser(User user) {
        return dayRepo.findAllByUserOrderByDateDesc(user);
    }
}
