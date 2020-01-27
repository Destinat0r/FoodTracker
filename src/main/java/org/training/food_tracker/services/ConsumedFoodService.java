package org.training.food_tracker.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.training.food_tracker.dto.FoodDTO;
import org.training.food_tracker.model.ConsumedFood;
import org.training.food_tracker.model.Day;
import org.training.food_tracker.model.User;
import org.training.food_tracker.repo.ConsumedFoodRepo;
import org.training.food_tracker.utils.ContextUtils;

import java.time.LocalTime;

@Service
public class ConsumedFoodService {

    private ConsumedFoodRepo consumedFoodRepo;

    private DayService dayService;

    @Autowired
    public ConsumedFoodService(ConsumedFoodRepo consumedFoodRepo, DayService dayService) {
        this.consumedFoodRepo = consumedFoodRepo;
        this.dayService = dayService;
    }

    public void registerConsumption(FoodDTO foodDTO) {
        User user = ContextUtils.getPrincipal();
        Day day = dayService.getCurrentDayOfUser(user);

        consumedFoodRepo.save(
                ConsumedFood.builder()
                        .food(foodDTO.getName())
                        .amount(foodDTO.getAmount())
                        .totalCalories(foodDTO.getTotalCalories())
                        .day(day)
                        .time(LocalTime.now())
                        .build()
        );
    }

}
