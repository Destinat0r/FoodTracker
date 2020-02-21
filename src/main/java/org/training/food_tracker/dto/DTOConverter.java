package org.training.food_tracker.dto;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.training.food_tracker.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Log4j2

public class DTOConverter {

    public static UserDTO userToUserDTO(User user) {
        return UserDTO.builder()
                       .username(user.getUsername())
                       .email(user.getEmail())
                       .password(user.getPassword())
                       .firstName(user.getFirstName())
                       .lastName(user.getLastName())
                       .dailyNorm(user.getDailyNormCalories())
                       .role(user.getRole())
                       .build();
    }

    public static BiometricsDTO biometricsToBiometricsDTO(Biometrics biometrics) {
        return BiometricsDTO.builder()
                       .age(biometrics.getAge())
                       .height(biometrics.getHeight())
                       .weight(biometrics.getWeight())
                       .lifestyle(biometrics.getLifestyle().toString())
                       .sex(biometrics.getSex().toString()).build();
    }

    public static FoodDTO foodToFoodDTO(Food food) {
        return FoodDTO.builder()
                       .name(food.getName())
                       .totalCalories(food.getCalories())
                       .build();
    }

    public static List<FoodDTO> foodsToFoodDTOs(List<Food> foods) {
        List<FoodDTO> foodDTOs = new ArrayList<>();
        foodDTOs.addAll(foods.stream().map(DTOConverter::foodToFoodDTO).collect(Collectors.toList()));
        log.debug("foodsToFoodDTOs() :: foodsDTOs: {}", foodDTOs);
        return foodDTOs;
    }

    public static DayDTO dayToDayDTO(Day day) {
        return DayDTO.builder()
                    .date(day.getDate())
                    .caloriesConsumed(day.getCaloriesConsumed())
                    .exceededCalories(day.getExceededCalories())
                    .consumedFoodDTOs(DTOConverter.consumedFoodsToConsumedFoodDTOs(day.getConsumedFoods()))
                    .isDailyNormExceeded(day.isDailyNormExceeded())
                    .build();
    }

    public static List<DayDTO> daysToDaysDTOs(List<Day> days) {
        return days.stream().map(DTOConverter::dayToDayDTO).collect(Collectors.toList());
    }

    public static ConsumedFoodDTO consumedFoodToConsumedFoodDTO(ConsumedFood consumedFood) {
        return ConsumedFoodDTO.builder()
                       .name(consumedFood.getName())
                       .amount(consumedFood.getAmount())
                       .totalCalories(consumedFood.getTotalCalories())
                       .time(consumedFood.getTime())
                       .build();
    }

    public static List<ConsumedFoodDTO> consumedFoodsToConsumedFoodDTOs(List<ConsumedFood> consumedFoods) {
        return consumedFoods.stream()
                       .map(DTOConverter::consumedFoodToConsumedFoodDTO)
                       .collect(Collectors.toList());
    }

    private static boolean checkIfDailyNormExceeded(BigDecimal exceededCalories) {
        return exceededCalories.compareTo(new BigDecimal(0)) > 0;
    }

    private static BigDecimal findExceededCalories(BigDecimal totalCalories, BigDecimal dailyNorm) {
        return dailyNorm.compareTo(totalCalories) > 0 ? new BigDecimal(0) : totalCalories.subtract(dailyNorm);
    }

    private static BigDecimal sumTotalCalories(List<ConsumedFood> foodDTOs) {
        BigDecimal sum = new BigDecimal(0);

        for (ConsumedFood food : foodDTOs) {
            sum = sum.add(food.getTotalCalories());
        }
        return sum;
    }
}
