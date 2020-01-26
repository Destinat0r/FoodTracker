package org.training.food_tracker.controller;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.training.food_tracker.dto.EventDTOsPack;
import org.training.food_tracker.dto.FoodDTO;
import org.training.food_tracker.repo.exceptions.UserNotFoundException;
import org.training.food_tracker.services.EventService;
import org.training.food_tracker.services.FoodService;
import org.training.food_tracker.services.UserService;

import java.util.List;

@Slf4j
@Log4j2
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
    public String addToCommonFood(@ModelAttribute("food") FoodDTO food) {
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
            model.addAttribute("userId", id);
        } catch (UserNotFoundException e) {
            model.addAttribute("message", e.getMessage());
            return "errors/no_such_user";
        }

        return "admin/user_profile";
    }

    @GetMapping("/history/user/{id}")
    public String getUserHistory(@PathVariable("id") Long id, Model model) {
        log.debug("Getting history of user with id: {}", id);

        log.debug("Getting eventDTOsPacks");
        List<EventDTOsPack> eventDTOsPacks = eventService.getEventDTOsPacksByUserId(id);

        model.addAttribute("eventDTOsPacks", eventDTOsPacks);
        try {
            model.addAttribute("userDTO", userService.getUserDTOById(id));
        } catch (UserNotFoundException e) {
            model.addAttribute("userId", id);
            log.error("Failed to found history of user with id {}", id, e);
            return "../public/error/no_such_user";
        }
        model.addAttribute("userId", id);

        return "admin/user_history";
    }
}
