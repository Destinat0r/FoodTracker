package org.training.food_tracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.training.food_tracker.dto.ConsumedStatsDTO;
import org.training.food_tracker.model.ConsumedFood;
import org.training.food_tracker.model.Day;
import org.training.food_tracker.model.User;
import org.training.food_tracker.repo.DayRepo;
import org.training.food_tracker.repo.exceptions.DayNotFoundException;
import org.training.food_tracker.utils.ContextUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            return optionalDay.get();
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

    public List<Day> getAllDaysByUser(User user) {
        return dayRepo.findAllByUser(user);
    }

    public BigDecimal getTotalCaloriesOfDay(Day day) {
        BigDecimal totalCalories = new BigDecimal(0);
        for (ConsumedFood food : day.getConsumedFoods()) {
            totalCalories = totalCalories.add(food.getTotalCalories());
        }
        return totalCalories;
    }

    public ConsumedStatsDTO getDayStatsForUser(User user, Day day) {
        BigDecimal userDailyNorm = user.getBiometrics().getDailyNorm();
        BigDecimal currentDayTotalCalories = day.getTotalCalories();
        boolean isNormExceeded = false;
        BigDecimal exceededCalories;

        if (userDailyNorm.compareTo(currentDayTotalCalories) > 0) {
            exceededCalories = new BigDecimal(0);
        } else {
            exceededCalories = currentDayTotalCalories.subtract(userDailyNorm);
            isNormExceeded = true;
        }
        return ConsumedStatsDTO.builder()
                       .caloriesConsumed(currentDayTotalCalories)
                       .isDailyNormExceeded(isNormExceeded)
                       .exceededCalories(exceededCalories)
                       .build();
    }
}
