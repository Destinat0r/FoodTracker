package org.training.food_tracker.controller;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.training.food_tracker.dto.FoodDTO;
import org.training.food_tracker.dto.UserDTO;
import org.training.food_tracker.model.User;
import org.training.food_tracker.repo.exceptions.UserNotFoundException;
import org.training.food_tracker.services.defaults.BiometricServiceDefault;
import org.training.food_tracker.services.defaults.DayServiceDefault;
import org.training.food_tracker.services.defaults.FoodServiceDefault;
import org.training.food_tracker.services.defaults.UserServiceDefault;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Log4j2
@Controller
@RequestMapping("/admin")
public class AdminController {

    private FoodServiceDefault foodServiceDefault;
    private UserServiceDefault userServiceDefault;
    private BiometricServiceDefault biometricServiceDefault;
    private DayServiceDefault dayServiceDefault;

    @Autowired
    public AdminController(FoodServiceDefault foodServiceDefault, UserServiceDefault userServiceDefault, BiometricServiceDefault biometricServiceDefault,
            DayServiceDefault dayServiceDefault) {
        this.foodServiceDefault = foodServiceDefault;
        this.userServiceDefault = userServiceDefault;
        this.biometricServiceDefault = biometricServiceDefault;
        this.dayServiceDefault = dayServiceDefault;
    }

    @GetMapping("/main")
    public String getMain() {
        return "admin/index";
    }

    @GetMapping("/food_list")
    public String getFoodList(Model model) {
        log.debug("loading food...");
        List<FoodDTO> allFood = foodServiceDefault.findAllCommonInDtos();
        model.addAttribute("allFood", allFood);
        model.addAttribute("food", new FoodDTO());
        return "admin/food_list";
    }

    @PostMapping("/food/add")
    public String addToCommonFood(@Valid FoodDTO food) {
        log.debug("Obtained entered food {}, adding to DB", food);
        foodServiceDefault.add(food);
        return "redirect:/admin/food_list";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userServiceDefault.findAll());
        return "admin/users";
    }

    @GetMapping("/user")
    public String getUserProfile(@RequestParam Long id, Model model) {
        log.debug("Getting user with id: {}", id);
        try {
            model.addAttribute("userDTO", userServiceDefault.getUserDTOById(id));
            model.addAttribute("userId", id);
        } catch (UserNotFoundException e) {
            model.addAttribute("message", e.getMessage());
            return "errors/no_such_user";
        }
        log.debug("User has been found. Returning user page");
        return "admin/user_profile";
    }

    @PostMapping("/user/update/")
    public String updateUser(@Valid UserDTO userDTO, BindingResult bindingResult) {

            Long id = userDTO.getId();
            log.debug("Updating user {} with id {}", userDTO, id);

            if (bindingResult.hasErrors()) {
                log.warn("Errors in input: {}", bindingResult.getAllErrors());
                log.warn(" Redirecting back to profile");
                return "user/profile";
            }

            log.debug("updating biometrics");
            biometricServiceDefault.update(userDTO);

            log.debug("Updating user");
            userServiceDefault.update(userDTO);

            return "redirect:/admin/user?id=" + id;
    }

    @GetMapping("/history/user/{id}")
    public String getUserHistory(@PathVariable("id") Long id, Model model) {
        log.debug("Getting history of user with id: {}", id);

        log.debug("Getting user by id");

        User user;

        try {
            user = userServiceDefault.findById(id);
        } catch (UserNotFoundException e) {
            log.error("User not found by id: {}", id, e);
            return "../public/error/no_such_user";
        }

        model.addAttribute("daysAndStats" , dayServiceDefault.getDaysToConsumeStatsForUser(user));
        model.addAttribute("daysOfUser", dayServiceDefault.getAllDaysByUser(user));
        model.addAttribute("userName", user.getUsername());
        model.addAttribute("dailyNorm", user.getBiometrics().getDailyNorm());

        return "admin/user_history";
    }
}
