package all.that.matters.controller;

import all.that.matters.domain.*;
import all.that.matters.services.FoodService;
import all.that.matters.services.StatisticService;
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

@Controller
@RequestMapping("/food")
public class FoodController {

    private FoodService foodService;
    private StatisticService statisticService;

    @Autowired
    public FoodController(FoodService foodService, StatisticService statisticService) {
        this.foodService = foodService;
        this.statisticService = statisticService;
    }

    @GetMapping("/main")
    public String getMain(Model model ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = (User) userDetails;

        if (user.getRoles().contains(Role.ADMIN)) {
            return "admin_main";
        }

        List<Food> usersFood = foodService.findAllByOwner(user);
        List<Statistic> todayStats = statisticService.findForToday();

        model.addAttribute("user", user);
        model.addAttribute("usersFood", usersFood);
        model.addAttribute("todayStats", todayStats);


        return "main";
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getAllFoods(Model model) {

        List<Food> allFood = foodService.findAll();
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

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user.getAuthorities().contains(Role.USER)) {
            food.setOwner(user);
            foodService.add(food);
            return "redirect:/food/main";
        }

        foodService.add(food);

        return "redirect:/food/main";
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

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Statistic stat = Statistic.builder()
                                 .user(user)
                                 .food(food)
                                 .action(Action.CONSUME)
                                 .amount(amount)
                                 .timestamp(LocalDateTime.now())
                                 .build();

        statisticService.create(stat);

        return "redirect:/food/main";
    }
}

