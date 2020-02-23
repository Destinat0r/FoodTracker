package org.training.food.tracker.services;

import org.springframework.stereotype.Service;
import org.training.food.tracker.model.ConsumedFood;
import org.training.food.tracker.model.Day;

import java.util.List;

@Service
public interface ConsumedFoodService {
    void registerConsumption(ConsumedFood consumedFood);

    void calculateCaloriesByAmount(ConsumedFood consumedFood);

    List<ConsumedFood> findAllByDay(Day day);

    void delete(ConsumedFood consumedFood);
}
