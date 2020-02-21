package org.training.food_tracker.services.defaults;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.training.food_tracker.model.*;
import org.training.food_tracker.repo.FoodRepo;
import org.training.food_tracker.dto.FoodDTO;
import org.training.food_tracker.services.FoodService;
import org.training.food_tracker.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Log4j2

@Service
public class FoodServiceDefault implements FoodService {

    private FoodRepo foodRepo;

    @Autowired
    public FoodServiceDefault(FoodRepo foodRepo) {
        this.foodRepo = foodRepo;
    }

    @Override public void create(Food food) {
        foodRepo.save(food);
    }

    @Override public List<Food> findAllCommonExcludingPersonalByUserId(Long userId) {
        return foodRepo.findAllCommonExcludingPersonalByUserId(userId);
    }

    @Override public List<Food> findAllCommon() {
        return foodRepo.findAllCommon();
    }

    @Override public List<Food> findAllByOwner(User owner) {
        return foodRepo.findAllByOwnerOrderByIdDesc(owner);
    }

    @Override public void deleteByNameAndUserId(String foodName, User user) {
        foodRepo.deleteByNameAndOwner(foodName, user);
    }

    @Override public void deleteCommonFoodByName(String foodName) {
        foodRepo.deleteCommonFoodByName(foodName);
    }
}
