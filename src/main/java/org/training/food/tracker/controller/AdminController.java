package org.training.food.tracker.controller;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.training.food.tracker.dto.FoodDTO;
import org.training.food.tracker.dto.UserDTO;
import org.training.food.tracker.model.User;
import org.training.food.tracker.repo.exceptions.UserNotFoundException;
import org.training.food.tracker.services.BiometricService;
import org.training.food.tracker.services.DayService;
import org.training.food.tracker.services.UserService;
import org.training.food.tracker.services.defaults.DayServiceDefault;
import org.training.food.tracker.dto.BiometricsDTO;
import org.training.food.tracker.dto.DTOConverter;
import org.training.food.tracker.services.FoodService;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Log4j2
@Controller
@RequestMapping("/admin")
public class AdminController {

    private FoodService foodService;
    private UserService userService;
    private BiometricService biometricService;
    private DayService dayService;

    @Autowired
    public AdminController(FoodService foodService, UserService userService, BiometricService biometricService,
            DayServiceDefault dayService) {
        this.foodService = foodService;
        this.userService = userService;
        this.biometricService = biometricService;
        this.dayService = dayService;
    }

    @GetMapping("/main")
    public String getMain() {
        return "admin/index";
    }

    @GetMapping("/food_list")
    public String getFoodList(Model model) {
        log.debug("loading food...");
        List<FoodDTO> allFood = DTOConverter.foodsToFoodDTOs(foodService.findAllCommon());

        model.addAttribute("allFood", allFood);
        model.addAttribute("food", new FoodDTO());
        return "admin/food_list";
    }

    @PostMapping("/food/add")
    public String addToCommonFood(@Valid FoodDTO foodDTO) {
        log.debug("Obtained entered food {}, adding to DB", foodDTO);
        foodService.create(DTOConverter.foodDTOtoFood(foodDTO));
        return "redirect:/admin/food_list";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/users";
    }

    @GetMapping("/user")
    public String getUserProfile(@RequestParam Long id, Model model) {
        log.debug("Getting user with id: {}", id);
        try {
            UserDTO userDTO = DTOConverter.userToUserDTO(userService.findById(id));
            model.addAttribute("userDTO", userDTO);
            model.addAttribute("userId", id);
        } catch (UserNotFoundException e) {
            model.addAttribute("message", e.getMessage());
            return "errors/no_such_user";
        }
        log.debug("User has been found. Returning user page");
        return "admin/user_profile";
    }

    @PostMapping("/user/update/")
    public String updateUser(
            @Valid UserDTO userDTO,
            BindingResult userBindingResult,
            @Valid BiometricsDTO biometricsDTO,
            BindingResult biometricsBindingResult
    ) {

            Long id = userDTO.getId();
            log.debug("Updating user {} with id {}", userDTO, id);

            if (userBindingResult.hasErrors() || biometricsBindingResult.hasErrors()) {
                log.warn("Errors in input: {}", userBindingResult.getAllErrors());
                log.warn(" Redirecting back to profile");
                return "user/profile";
            }

            log.debug("updating biometrics");
            biometricService.update(DTOConverter.biometricsDTOtoBiometrics(biometricsDTO));

            log.debug("Updating user");
            userService.update(DTOConverter.userDTOtoUser(userDTO));

            return "redirect:/admin/user?id=" + id;
    }

    @GetMapping("/history/user/{id}")
    public String getUserHistory(@PathVariable("id") Long id, Model model) {
        log.debug("Getting history of user with id: {}", id);

        log.debug("Getting user by id");

        User user;

        try {
            user = userService.findById(id);
        } catch (UserNotFoundException e) {
            log.error("User not found by id: {}", id, e);
            return "../public/error/no_such_user";
        }

        model.addAttribute("dayDTOs", DTOConverter.daysToDaysDTOs(dayService.getAllDaysByUser(user)));
        model.addAttribute("userName", user.getUsername());
        model.addAttribute("dailyNorm", user.getDailyNormCalories());

        return "admin/user_history";
    }
}
