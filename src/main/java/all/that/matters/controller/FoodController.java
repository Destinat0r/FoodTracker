package all.that.matters.controller;

import all.that.matters.domain.Food;
import all.that.matters.domain.Role;
import all.that.matters.domain.Statistic;
import all.that.matters.domain.User;
import all.that.matters.services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/food")
public class FoodController {

    private FoodService foodService;

    @Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
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
            return "redirect:/main";
        }

        foodService.add(food);

        return "redirect:/main";
    }

    @PostMapping("/consume")
    public String consume(
            @Valid Food food,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
        }

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Statistic stat = Statistic.builder().user(user).food(food).build();

        return "redirect:/main";
    }
}

