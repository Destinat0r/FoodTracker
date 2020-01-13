package all.that.matters.services;

import all.that.matters.repo.FoodNotFoundException;
import all.that.matters.repo.FoodRepo;
import all.that.matters.model.Food;
import all.that.matters.model.User;
import all.that.matters.dto.ConsumedStatsDto;
import all.that.matters.dto.FoodDto;
import all.that.matters.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class FoodService {

    private FoodRepo foodRepo;
    private EventService eventService;
    private UserService userService;
    private ExceededNormEventService exceededNormEventService;

    @Autowired
    public FoodService(FoodRepo foodRepo, EventService eventService, UserService userService,
            ExceededNormEventService exceededNormEventService) {
        this.foodRepo = foodRepo;
        this.eventService = eventService;
        this.userService = userService;
        this.exceededNormEventService = exceededNormEventService;
    }

    public List<Food> findAll() {
        return foodRepo.findAll();
    }

    public void add(FoodDto foodDto) {
        Food food = Food.builder()
                        .name(foodDto.getName())
                        .calories(foodDto.getCalories())
                        .owner(ContextUtils.getPrincipal())
                        .build();

        foodRepo.save(food);
    }

    public List<FoodDto> findAllByOwner(User user) {
        List<FoodDto> foodDtos = new ArrayList<>();
        foodRepo.findByOwner(user.getId()).forEach(
                food -> foodDtos.add(
                            FoodDto.builder()
                                    .name(food.getName())
                                    .calories(food.getCalories())
                                    .build())
        );
        return foodDtos;
    }

    public Optional<Food> findById(Long id) {
        return foodRepo.findById(id);
    }

    public List<FoodDto> findAllCommonFoodInDtos() {
        List<FoodDto> commonFoodDtos = new ArrayList<>();
        foodRepo.findAllCommon().forEach(food -> commonFoodDtos.add(
                FoodDto.builder()
                        .name(food.getName())
                        .calories(food.getCalories())
                        .build()
        ));

        return commonFoodDtos;
    }

    public void registerConsumption(FoodDto foodDto) {
        User user = ContextUtils.getPrincipal();
        Food food = foodDtoToFood(foodDto);

        eventService.createConsumeEvent(food, foodDto.getAmount(), user);

        ConsumedStatsDto stats = getConsumedStatsForUserAndDate(user, LocalDate.now());

        if (stats.isDailyNormExceeded()) {
            exceededNormEventService.createExceededNormEvent(user, stats.getCaloriesConsumed());
        }
    }

    public ConsumedStatsDto getConsumedStatsForUserAndDate(User user, LocalDate date) {
        Double caloriesConsumed = getConsumedCaloriesForToday(user.getId(), date);
        boolean isDailyNormExceeded = getUserDailyNorm() < caloriesConsumed;
        Double exceededCalories = isDailyNormExceeded ? caloriesConsumed - getUserDailyNorm() : 0.0;
        return ConsumedStatsDto.builder()
                       .caloriesConsumed(caloriesConsumed)
                       .isDailyNormExceeded(isDailyNormExceeded)
                       .exceededCalories(exceededCalories)
                       .build();
    }

    public void remove(Food food) {
        foodRepo.delete(food);
    }

    private Double getUserDailyNorm() {
        return ContextUtils.getPrincipal().getBiometrics().getDailyNorm();
    }

    public Double getConsumedCaloriesForToday(Long userId, LocalDate date) {
        return eventService.getTotalConsumedCaloriesByUserIdAndDate(userId, date);
    }

    public FoodDto foodToFoodDto(Food food) {
        return FoodDto.builder()
                       .name(food.getName())
                       .calories(food.getCalories())
                       .build();
    }

    public Food foodDtoToFood(FoodDto foodDto) {
        return foodRepo.findByName(foodDto.getName())
                       .orElseThrow(() -> new FoodNotFoundException("Food with name: " + foodDto.getName() + " not found."));
    }
}
