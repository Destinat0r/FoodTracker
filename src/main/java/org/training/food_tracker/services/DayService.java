package org.training.food_tracker.services;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.training.food_tracker.dto.ConsumeStatsDTO;
import org.training.food_tracker.model.ConsumedFood;
import org.training.food_tracker.model.Day;
import org.training.food_tracker.model.User;
import org.training.food_tracker.repo.DayRepo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Log4j2

@Service
public class DayService {

    private DayRepo dayRepo;

    @Autowired
    public DayService(DayRepo dayRepo) {
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
                         .totalCalories(new BigDecimal(0))
                         .user(user)
                         .build();
        dayRepo.save(newDay);
        return newDay;
    }

    private void sortConsumedFoodByTimeDesc(List<ConsumedFood> foods) {
        foods.sort((food1, food2) -> (food2.getTime().toSecondOfDay() - food1.getTime().toSecondOfDay()));
    }

    public Map<Day, ConsumeStatsDTO> getDaysToConsumeStatsForUser(User user) {
        return mapDaysToConsumeStats(getAllDaysByUser(user));
    }

    private Map<Day, ConsumeStatsDTO> mapDaysToConsumeStats(List<Day> days) {
        Map<Day, ConsumeStatsDTO> dayToConsumeStats = new LinkedHashMap<>();
        days.forEach(day -> dayToConsumeStats.put(day, getConsumeStatsForDay(day)));
        return dayToConsumeStats;
    }

    public List<Day> getAllDaysByUser(User user) {
        return dayRepo.findAllByUserOrderByDateDesc(user);
    }

    public BigDecimal getTotalCaloriesOfDay(Day day) {
        BigDecimal totalCalories = new BigDecimal(0);
        for (ConsumedFood food : day.getConsumedFoods()) {
            totalCalories = totalCalories.add(food.getTotalCalories());
        }
        return totalCalories;
    }

    public ConsumeStatsDTO getConsumeStatsForDay(Day day) {
        log.debug("Getting day statistics for user");

        BigDecimal userDailyNorm = day.getUser().getBiometrics().getDailyNorm();
        log.debug("User's daily norm {}", userDailyNorm);

        BigDecimal currentDayTotalCalories = day.getTotalCalories();
        log.debug("Current day total calories: {}", currentDayTotalCalories);
        boolean isNormExceeded = false;
        BigDecimal exceededCalories;

        if (userDailyNorm.compareTo(currentDayTotalCalories) > 0) {
            exceededCalories = new BigDecimal(0);
        } else {
            exceededCalories = currentDayTotalCalories.subtract(userDailyNorm);
            isNormExceeded = true;
        }
        log.debug("exceeded calories: {}", exceededCalories);

        return ConsumeStatsDTO.builder()
                       .caloriesConsumed(currentDayTotalCalories)
                       .isDailyNormExceeded(isNormExceeded)
                       .exceededCalories(exceededCalories)
                       .build();
    }
}
