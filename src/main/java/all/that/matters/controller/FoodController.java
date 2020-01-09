package all.that.matters.controller;

import all.that.matters.domain.*;
import all.that.matters.services.FoodService;
import all.that.matters.services.StatisticService;
import all.that.matters.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

        User user = getPrincipal();

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

        User user = getPrincipal();

        if (user.getAuthorities().contains(Role.USER)) {
            food.setOwner(user);
            foodService.add(food);
            return "redirect:/food/main";
        }

        foodService.add(food);

        return "redirect:/food/all";
    }

    private User getPrincipal() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
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

        User user = getPrincipal();

        List<Statistic> todayStats = statisticService.findForToday();
        Double consumedToday = todayStats.stream().mapToDouble(statistic -> statistic.getFood().getCalories()).sum();

        //TODO remade using session or something
        Optional<Food> optionalFood = foodService.findById(food.getId());
        Double currentFoodCalories = optionalFood.isPresent() ? optionalFood.get().getCalories() : 0;

        user.getBiometrics().setConsumedToday(consumedToday + currentFoodCalories);

        userService.save(user);

        Statistic stat = Statistic.builder()
                                 .user(user)
                                 .food(food)
                                 .action(Action.CONSUME)
                                 .amount(amount)
                                 .timestamp(LocalDateTime.now())
                                 .build();

        statisticService.create(stat);

        if (user.getBiometrics().getDailyNorm() > consumedToday) {
            Statistic exceed_stat = Statistic.builder()
                                     .user(user)
                                     .food(food)
                                     .action(Action.EXCEED_DAILY_LIMIT)
                                     .timestamp(LocalDateTime.now())
                                     .build();

            statisticService.create(exceed_stat);
        }

        return "redirect:/food/main";
    }
}

