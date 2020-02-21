package org.training.food_tracker.services;

import org.training.food_tracker.model.ConsumedFood;
import org.training.food_tracker.model.Day;
import org.training.food_tracker.model.User;

import java.util.List;

public interface DayService {
    Day getCurrentDayOfUser(User user);

    List<Day> getAllDaysByUser(User user);

    void updateDay(Day day, ConsumedFood consumedFood);
}
