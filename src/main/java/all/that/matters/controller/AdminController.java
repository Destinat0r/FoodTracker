package all.that.matters.controller;

import all.that.matters.dto.FoodDto;
import all.that.matters.model.User;
import all.that.matters.services.FoodService;
import all.that.matters.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private FoodService foodService;
    private UserService userService;

    @Autowired
    public AdminController(FoodService foodService, UserService userService) {
        this.foodService = foodService;
        this.userService = userService;
    }

    @GetMapping("/main")
    public String getMain(Model model) {
        return "admin/index";
    }

    @GetMapping("/food_list")
    public String getFoodList(Model model) {
        List<FoodDto> allFood = foodService.findAllCommonFoodInDtos();
        model.addAttribute("allFood", allFood);
        return "admin/food_list";
    }

    @PostMapping("/food/add")
    public String addToCommonFood(
            @ModelAttribute("food") FoodDto food,
            Model model) {
        foodService.add(food);
        return "redirect:/admin/food_list";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/user")
    public String getUserProfile(@RequestParam Long id, Model model) {
        Optional<User> user = userService.findById(id);
        user.ifPresent((user1) -> model.addAttribute("user", user1));
        return "profile";
    }
}
