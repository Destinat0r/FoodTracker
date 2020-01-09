package all.that.matters.controller;

import all.that.matters.domain.*;
import all.that.matters.services.FoodService;
import all.that.matters.services.StatisticService;
import all.that.matters.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/food")
public class FoodController {

    private FoodService foodService;
    private StatisticService statisticService;
    private UserService userService;

    @Autowired
    public FoodController(FoodService foodService, StatisticService statisticService, UserService userService) {
        this.foodService = foodService;
        this.statisticService = statisticService;
        this.userService = userService;
    }

    @GetMapping("/main")
    public String getMain(Model model ) {

        User user = ControllerUtils.getPrincipal();

        List<Food> usersFood = foodService.findAllByOwner(user);
        List<Statistic> todayStats = statisticService.findForToday();
        Double consumedToday = todayStats.stream().mapToDouble(statistic -> statistic.getFood().getCalories()).sum();

        if (consumedToday > user.getBiometrics().getDailyNorm()) {
            model.addAttribute("exceeded", true);
        }

        model.addAttribute("consumedToday", consumedToday);
        model.addAttribute("user", user);
        model.addAttribute("allAvailableFood", foodService.findAllCommon());
        model.addAttribute("usersFood", usersFood);
        model.addAttribute("todayStats", todayStats);

        return "main";
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getAllFoods(Model model) {

        List<Food> allFood = foodService.findAllCommon();
        model.addAttribute("allFood", allFood);

        return "all_food";
    }

    @PostMapping("/add")
    public String add(
            @Valid Food food,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
        }

        User user = ControllerUtils.getPrincipal();

        if (user.getAuthorities().contains(Role.USER)) {
            food.setOwner(user);
            foodService.add(food);
            return "redirect:/food/main";
        }

        foodService.add(food);

        return "redirect:/food/all";
    }

    @PostMapping("/consume")
    public String consume(
            @ModelAttribute("food") Food food,
            @RequestParam("amount") Double amount,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
        }

        User user = ControllerUtils.getPrincipal();

        //TODO remade using session or something
        Double caloriesConsumedToday = getCaloriesConsumedToday() + getCurrentFoodCalories(food);

        user.getBiometrics().setConsumedToday(caloriesConsumedToday);
        userService.save(user);

        createConsumeStat(food, amount, user);
        ifLimitExceededCreateStat(food, user, caloriesConsumedToday);

        return "redirect:/food/main";
    }

    private void createConsumeStat(@ModelAttribute("food") Food food, @RequestParam("amount") Double amount,
            User user) {
        Statistic stat = Statistic.builder()
                                 .user(user)
                                 .food(food)
                                 .action(Action.CONSUME)
                                 .amount(amount)
                                 .timestamp(LocalDateTime.now())
                                 .build();

        statisticService.create(stat);
    }

    private void ifLimitExceededCreateStat(@ModelAttribute("food") Food food, User user, Double caloriesConsumedToday) {
        if (user.getBiometrics().getDailyNorm() > caloriesConsumedToday) {
            Statistic exceed_stat = Statistic.builder()
                                     .user(user)
                                     .food(food)
                                     .action(Action.EXCEED_DAILY_LIMIT)
                                     .timestamp(LocalDateTime.now())
                                     .build();

            statisticService.create(exceed_stat);
        }
    }

    private Double getCurrentFoodCalories(@ModelAttribute("food") Food food) {
        Optional<Food> optionalFood = foodService.findById(food.getId());
        return optionalFood.isPresent() ? optionalFood.get().getCalories() : 0;
    }

    private Double getCaloriesConsumedToday() {
        List<Statistic> todayStats = statisticService.findForToday();
        return todayStats.stream().mapToDouble(statistic -> statistic.getFood().getCalories()).sum();
    }
}
