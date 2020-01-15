package all.that.matters.controller;

import all.that.matters.dto.EventDTOsPack;
import all.that.matters.dto.FoodDTO;
import all.that.matters.repo.UserNotFoundException;
import all.that.matters.services.EventService;
import all.that.matters.services.FoodService;
import all.that.matters.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private FoodService foodService;
    private UserService userService;
    private EventService eventService;

    @Autowired
    public AdminController(FoodService foodService, UserService userService, EventService eventService) {
        this.foodService = foodService;
        this.userService = userService;
        this.eventService = eventService;
    }

    @GetMapping("/main")
    public String getMain(Model model) {
        return "admin/index";
    }

    @GetMapping("/food_list")
    public String getFoodList(Model model) {
        List<FoodDTO> allFood = foodService.findAllCommonFoodInDtos();
        model.addAttribute("allFood", allFood);
        return "admin/food_list";
    }

    @PostMapping("/food/add")
    public String addToCommonFood(
            @ModelAttribute("food") FoodDTO food,
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
        try {
            model.addAttribute("userDTO", userService.getUserDTOById(id));
        } catch (UserNotFoundException e) {
            model.addAttribute("message", e.getMessage());
            return "errors/no_such_user";
        }

        return "admin/profile";
    }

    @GetMapping("/user/history")
    public String getUserHistory(@RequestParam Long id, Model model) {
        List<EventDTOsPack> eventDTOsPacks = eventService.getEventDTOsPacksByUserId(id);

        model.addAttribute("eventDTOsPacks", eventDTOsPacks);
        try {
            model.addAttribute("userDTO", userService.getUserDTOById(id));
        } catch (UserNotFoundException e) {
            model.addAttribute("message", e.getMessage());
            return "errors/no_such_user";
        }
        model.addAttribute("userId", id);

        return "admin/user/history";
    }
}
