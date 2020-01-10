package all.that.matters.controller;

import all.that.matters.domain.*;
import all.that.matters.services.FoodService;
import all.that.matters.services.EventService;
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
    private EventService eventService;
    private UserService userService;

    @Autowired
    public FoodController(FoodService foodService, EventService eventService, UserService userService) {
        this.foodService = foodService;
        this.eventService = eventService;
        this.userService = userService;
    }

    @GetMapping("/main")
    public String getMain(Model model ) {

        User user = ControllerUtils.getPrincipal();

        List<Food> usersFood = foodService.findAllByOwner(user);
        List<Event> todayEvents = eventService.findForToday();
        Double consumedToday = todayEvents.stream().mapToDouble(event -> event.getFood().getCalories()).sum();

        if (consumedToday > user.getBiometrics().getDailyNorm()) {
            model.addAttribute("exceeded", true);
        }

        model.addAttribute("consumedToday", consumedToday);
        model.addAttribute("user", user);
        model.addAttribute("allAvailableFood", foodService.findAllCommon());
        model.addAttribute("usersFood", usersFood);
        model.addAttribute("todayEvents", todayEvents);

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

        createConsumeEvent(food, amount, user);
        ifLimitExceededCreateEvent(food, user, caloriesConsumedToday);

        return "redirect:/food/main";
    }

    private void createConsumeEvent(@ModelAttribute("food") Food food, @RequestParam("amount") Double amount,
            User user) {
        Event consume = Event.builder()
                                 .user(user)
                                 .food(food)
                                 .action(Action.CONSUME)
                                 .amount(amount)
                                 .timestamp(LocalDateTime.now())
                                 .build();

        eventService.create(consume);
    }

    private void ifLimitExceededCreateEvent(@ModelAttribute("food") Food food, User user, Double caloriesConsumedToday) {
        if (user.getBiometrics().getDailyNorm() < caloriesConsumedToday) {
            Event exceed = Event.builder()
                                     .user(user)
                                     .food(food)
                                     .action(Action.EXCEED_DAILY_LIMIT)
                                     .timestamp(LocalDateTime.now())
                                     .build();

            eventService.create(exceed);
        }
    }

    private Double getCurrentFoodCalories(@ModelAttribute("food") Food food) {
        Optional<Food> optionalFood = foodService.findById(food.getId());
        return optionalFood.isPresent() ? optionalFood.get().getCalories() : 0;
    }

    private Double getCaloriesConsumedToday() {
        List<Event> todayEvents = eventService.findForToday();
        return todayEvents.stream().mapToDouble(event -> event.getFood().getCalories()).sum();
    }
}
