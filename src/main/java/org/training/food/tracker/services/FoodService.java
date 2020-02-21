package org.training.food.tracker.services;

import org.springframework.stereotype.Service;
import org.training.food.tracker.model.Food;
import org.training.food.tracker.model.User;

import java.util.List;

@Service
public interface FoodService {
    void create(Food food);

    List<Food> findAllCommon();

    List<Food> findAllCommonExcludingPersonalByUserId(Long userId);

    List<Food> findAllByOwner(User user);

    void deleteByNameAndUserId(String foodName, User user);

    void deleteCommonFoodByName(String foodName);
}
