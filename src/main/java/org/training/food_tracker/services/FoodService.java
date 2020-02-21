package org.training.food_tracker.services;

import org.training.food_tracker.model.Food;
import org.training.food_tracker.model.User;

import java.util.List;

public interface FoodService {
    void create(Food food);

    List<Food> findAllCommon();

    List<Food> findAllCommonExcludingPersonalByUserId(Long userId);

    List<Food> findAllByOwner(User user);

    void deleteByNameAndUserId(String foodName, User user);

    void deleteCommonFoodByName(String foodName);
}
