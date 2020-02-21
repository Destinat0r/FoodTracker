package org.training.food.tracker.services;

import org.springframework.stereotype.Service;
import org.training.food.tracker.model.ConsumedFood;
import org.training.food.tracker.model.Day;
import org.training.food.tracker.model.User;

import java.util.List;

@Service
public interface DayService {
    Day getCurrentDayOfUser(User user);

    List<Day> getAllDaysByUser(User user);

    void updateDay(Day day, ConsumedFood consumedFood);
}
