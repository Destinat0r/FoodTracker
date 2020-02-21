package org.training.food_tracker.services;

import org.training.food_tracker.model.ConsumedFood;
import org.training.food_tracker.model.Day;

import java.util.List;

public interface ConsumedFoodService {
    void registerConsumption(ConsumedFood consumedFood);

    void calculateCaloriesByAmount(ConsumedFood consumedFood);

    List<ConsumedFood> findAllByDay(Day day);

    void delete(ConsumedFood consumedFood);
}
