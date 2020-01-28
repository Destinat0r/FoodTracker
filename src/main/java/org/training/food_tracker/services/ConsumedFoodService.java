package org.training.food_tracker.services;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.training.food_tracker.dto.FoodDTO;
import org.training.food_tracker.model.ConsumedFood;
import org.training.food_tracker.model.Day;
import org.training.food_tracker.model.User;
import org.training.food_tracker.repo.ConsumedFoodRepo;
import org.training.food_tracker.utils.ContextUtils;

import java.time.LocalTime;

@Slf4j
@Log4j2

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
        log.debug("method: registerConsumption");

        User user = ContextUtils.getPrincipal();

        Day day = dayService.getCurrentDayOfUser(user);
        log.debug("current day of the user: {}", day);

        log.debug("adding consumed calories to the current day:");
        day.addCalories(foodDTO.getTotalCalories().multiply(foodDTO.getAmount()));

        log.debug("saving consumed food from foodDTO: {}", foodDTO);
        consumedFoodRepo.save(
                ConsumedFood.builder()
                        .name(foodDTO.getName())
                        .amount(foodDTO.getAmount())
                        .totalCalories(foodDTO.getTotalCalories().multiply(foodDTO.getAmount()))
                        .day(day)
                        .time(LocalTime.now())
                        .build()
        );
    }

}
