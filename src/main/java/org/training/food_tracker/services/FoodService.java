package org.training.food_tracker.services;

import org.training.food_tracker.dto.ConsumedStatsDTO;
import org.training.food_tracker.model.Role;
import org.training.food_tracker.repo.FoodNotFoundException;
import org.training.food_tracker.repo.FoodRepo;
import org.training.food_tracker.model.Food;
import org.training.food_tracker.model.User;
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
    private UserService userService;
    private ExceededEventService exceededEventService;

    @Autowired
    public FoodService(FoodRepo foodRepo, EventService eventService, UserService userService,
            ExceededEventService exceededEventService) {
        this.foodRepo = foodRepo;
        this.eventService = eventService;
        this.userService = userService;
        this.exceededEventService = exceededEventService;
    }

    public void add(FoodDTO foodDTO) {
        Food food = Food.builder()
                        .name(foodDTO.getName())
                        .calories(foodDTO.getCalories())
                        .owner(getOwner())
                        .build();
        foodRepo.save(food);
    }

    private User getOwner() {
        User user = ContextUtils.getPrincipal();
        return user.getRoles().contains(Role.ADMIN) ? null : user;
    }

    public List<FoodDTO> findAllCommonFoodExcludingPersonalByUserIdInDTO(Long userId) {
        return findAllCommonFoodExcludingPersonalByUserId(userId).stream()
                       .map(this::foodToFoodDto)
                       .collect(Collectors.toList());
    }

    private List<Food> findAllCommonFoodExcludingPersonalByUserId(Long userId) {
        return foodRepo.findAllCommonExcludingPersonalByUserId(userId);
    }

    public List<FoodDTO> findAllByOwner(User user) {
        List<FoodDTO> foodDTOS = new ArrayList<>();
        foodRepo.findByOwner(user.getId()).forEach(
                food -> foodDTOS.add(
                            FoodDTO.builder()
                                    .name(food.getName())
                                    .calories(food.getCalories())
                                    .build())
        );
        return foodDTOS;
    }

    public Optional<Food> findById(Long id) {
        return foodRepo.findById(id);
    }

    public void removeByFoodNameAndUserId(String foodName, User user) {
        foodRepo.removeByNameAndOwner(foodName, user);
    }

    public List<FoodDTO> findAllCommonFoodInDtos() {
        return foodRepo.findAllCommon().stream().map(this::foodToFoodDto).collect(Collectors.toList());
    }

    public void registerConsumption(FoodDTO foodDTO) throws FoodNotFoundException {
        User user = ContextUtils.getPrincipal();
        Food food = foodDtoToFood(foodDTO);

        eventService.createConsumeEvent(food, foodDTO.getAmount(), user);

        ConsumedStatsDTO stats = getConsumedStatsForUserAndDate(user, LocalDate.now());

        if (stats.isDailyNormExceeded()) {
            exceededEventService.createExceededNormEvent(user, stats.getCaloriesConsumed());
        }
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

    public BigDecimal getConsumedCaloriesForToday(Long userId, LocalDate date) {
        return eventService.getTotalConsumedCaloriesByUserIdAndDate(userId, date);
    }

    public FoodDTO foodToFoodDto(Food food) {
        return FoodDTO.builder()
                       .name(food.getName())
                       .calories(food.getCalories())
                       .build();
    }

    public Food foodDtoToFood(FoodDTO foodDTO) throws FoodNotFoundException {
        return foodRepo.findByNameAndOwner(foodDTO.getName(), ContextUtils.getPrincipal())
                       .orElseThrow(() -> new FoodNotFoundException("Food with name: " + foodDTO.getName() + " not found."));
    }
}
