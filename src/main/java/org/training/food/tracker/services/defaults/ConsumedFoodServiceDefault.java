package org.training.food.tracker.services.defaults;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.training.food.tracker.model.ConsumedFood;
import org.training.food.tracker.model.Day;
import org.training.food.tracker.repo.ConsumedFoodRepo;
import org.training.food.tracker.services.ConsumedFoodService;

import java.util.List;

@Slf4j
@Log4j2

@Service
public class ConsumedFoodServiceDefault implements ConsumedFoodService {

    private ConsumedFoodRepo consumedFoodRepo;

    @Autowired
    public ConsumedFoodServiceDefault(ConsumedFoodRepo consumedFoodRepo, DayServiceDefault dayServiceDefault) {
        this.consumedFoodRepo = consumedFoodRepo;
    }

    @Override public void registerConsumption(ConsumedFood consumedFood) {
        consumedFoodRepo.save(consumedFood);
    }

    @Override public void calculateCaloriesByAmount(ConsumedFood consumedFood) {
        consumedFood.setTotalCalories(
                consumedFood.getTotalCalories()
                        .multiply(consumedFood.getAmount())
        );
    }

    @Override public List<ConsumedFood> findAllByDay(Day day) {
        return consumedFoodRepo.findAllByDay(day);
    }

    @Override public void delete(ConsumedFood consumedFood) {
        consumedFoodRepo.delete(consumedFood);
    }
}
