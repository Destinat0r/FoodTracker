package org.training.food_tracker.services;

import org.training.food_tracker.dto.ConsumedStatsDTO;
import org.training.food_tracker.model.*;
import org.training.food_tracker.repo.FoodRepo;
import org.training.food_tracker.dto.FoodDTO;
import org.training.food_tracker.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FoodService {

    private FoodRepo foodRepo;
    private EventService eventService;
    private ConsumedFoodService consumedFoodService;

    @Autowired
    public FoodService(FoodRepo foodRepo, EventService eventService, ConsumedFoodService consumedFoodService) {
        this.foodRepo = foodRepo;
        this.eventService = eventService;
        this.consumedFoodService = consumedFoodService;
    }

    public void add(FoodDTO foodDTO) {
        Food food = Food.builder()
                        .name(foodDTO.getName())
                        .calories(foodDTO.getTotalCalories())
                        .owner(getOwner())
                        .build();
        foodRepo.save(food);
    }

    public void registerConsumption(FoodDTO foodDTO) {
        consumedFoodService.registerConsumption(foodDTO);
    }


    private User getOwner() {
        User user = ContextUtils.getPrincipal();
        return user.getRole() == Role.ADMIN ? null : user;
    }

    public List<FoodDTO> findAllCommonExcludingPersonalByUserIdInDTO(Long userId) {
        return findAllCommonExcludingPersonalByUserId(userId).stream()
                       .map(this::foodToFoodDTO)
                       .collect(Collectors.toList());
    }

    private List<Food> findAllCommonExcludingPersonalByUserId(Long userId) {
        return foodRepo.findAllCommonExcludingPersonalByUserId(userId);
    }

    public List<FoodDTO> findAllByOwnerInDTOs(User user) {
        List<FoodDTO> foodDTOS = new ArrayList<>();
        foodRepo.findByOwnerOrderByIdDesc(user)
                .forEach(food -> foodDTOS.add(FoodDTO.builder()
                                                .name(food.getName())
                                                .totalCalories(food.getCalories())
                                                .build())
        );
        return foodDTOS;
    }

    public Optional<Food> findById(Long id) {
        return foodRepo.findById(id);
    }

    public void removeByNameAndUserId(String foodName, User user) {
        foodRepo.removeByNameAndOwner(foodName, user);
    }

    public List<FoodDTO> findAllCommonInDtos() {
        return foodRepo.findAllCommon().stream().map(this::foodToFoodDTO).collect(Collectors.toList());
    }

    public ConsumedStatsDTO getConsumedStatsForUserAndDate(User user, LocalDate date) {
        BigDecimal caloriesConsumed = getConsumedCaloriesForToday(user.getId(), date);
        boolean isDailyNormExceeded = getUserDailyNorm().compareTo(caloriesConsumed) < 0;
        BigDecimal exceededCalories = isDailyNormExceeded ?
                                              caloriesConsumed.subtract(getUserDailyNorm()) : new BigDecimal(0.0);

        return ConsumedStatsDTO.builder()
                       .caloriesConsumed(caloriesConsumed)
                       .isDailyNormExceeded(isDailyNormExceeded)
                       .exceededCalories(exceededCalories)
                       .build();
    }

    public void remove(Food food) {
        foodRepo.delete(food);
    }

    private BigDecimal getUserDailyNorm() {
        return ContextUtils.getPrincipal().getBiometrics().getDailyNorm();
    }

    private BigDecimal getConsumedCaloriesForToday(Long userId, LocalDate date) {
        return eventService.getTotalConsumedCaloriesByUserIdAndDate(userId, date);
    }

    private FoodDTO foodToFoodDTO(Food food) {
        return FoodDTO.builder()
                       .name(food.getName())
                       .totalCalories(food.getCalories())
                       .build();
    }

}
