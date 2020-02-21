package org.training.food.tracker.controller;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.training.food.tracker.dto.DTOConverter;
import org.training.food.tracker.dto.FoodDTO;
import org.training.food.tracker.dto.UserDTO;
import org.training.food.tracker.model.*;
import org.training.food.tracker.repo.exceptions.UserNotFoundException;
import org.training.food.tracker.services.*;
import org.training.food.tracker.services.defaults.BiometricServiceDefault;
import org.training.food.tracker.services.defaults.DayServiceDefault;
import org.training.food.tracker.services.defaults.FoodServiceDefault;
import org.training.food.tracker.services.defaults.UserServiceDefault;
import org.training.food.tracker.utils.ContextUtils;

import javax.validation.Valid;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Log4j2

@Controller
@RequestMapping("/user")
public class UserController {

    private FoodService foodService;
    private DayService dayService;
    private UserService userService;
    private ConsumedFoodService consumedFoodService;
    private BiometricService biometricService;

    @Autowired
    public void setFoodService(FoodServiceDefault foodService) {
        this.foodService = foodService;
    }

    @Autowired
    public void setUserService(UserServiceDefault userService) {
        this.userService = userService;
    }

    @Autowired
    public void setDayService(DayServiceDefault dayService) {
        this.dayService = dayService;
    }

    @Autowired
    public void setBiometricService(BiometricServiceDefault biometricService) {
        this.biometricService = biometricService;
    }

    @Autowired
    public void setConsumedFoodService(ConsumedFoodService consumedFoodService) {
        this.consumedFoodService = consumedFoodService;
    }

    @GetMapping("/main")
    public String getMain(Model model) {
        log.debug("loading user's main");

        User user = ContextUtils.getPrincipal();
        UserDTO userDTO = DTOConverter.userToUserDTO(user);

        log.debug("setting allCommonFood");
        List<FoodDTO> allCommonFoodDTOs = DTOConverter.foodsToFoodDTOs(foodService.findAllCommonExcludingPersonalByUserId(user.getId()));
        model.addAttribute("allCommonFoodDTOs", allCommonFoodDTOs);


        model.addAttribute("foodDTO", new FoodDTO());

        log.debug("setting usersFoodDTOs");
        List<FoodDTO> usersFoodDTOs = DTOConverter.foodsToFoodDTOs(foodService.findAllByOwner(user));
        model.addAttribute("usersFoodDTOs", usersFoodDTOs);

        log.debug("setting userDTO {}", userDTO);
        model.addAttribute("userDTO", userDTO);

        log.debug("getting current day");
        Day currentDay = dayService.getCurrentDayOfUser(user);

        model.addAttribute("currentDay", currentDay);

        return "user/main";
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getAllFoods(Model model) {
        List<FoodDTO> allFoodDTOs = DTOConverter.foodsToFoodDTOs(foodService.findAllCommon());
        model.addAttribute("allFood", allFoodDTOs);

        return "food_list";
    }

    @PostMapping("/add")
    public String add(FoodDTO userFoodDTO) {
        Food foodToAdd = DTOConverter.foodDTOtoFood(userFoodDTO);
        foodService.create(foodToAdd);
        return "redirect:/user/main";
    }

    @PostMapping(value = "/use", params = "consume")
    public String consume(
            @Valid FoodDTO foodDTO,
            BindingResult bindingResult) {

        log.debug("Obtained foodDTO from client: {} ", foodDTO);

        if (bindingResult.hasErrors()) {
            return "main";
        }

        ConsumedFood consumedFood = buildConsumedFood(foodDTO);

        log.debug("Registering consumption");
        consumedFoodService.registerConsumption(consumedFood);

        return "redirect:/user/main";
    }

    private ConsumedFood buildConsumedFood(@Valid FoodDTO foodDTO) {
        return ConsumedFood.builder()
                                            .name(foodDTO.getName())
                                            .amount(foodDTO.getAmount())
                                            .time(LocalTime.now())
                                            .totalCalories(foodDTO.getTotalCalories())
                                            .build();
    }

    @PostMapping(value = "/use", params = "delete")
    public String delete(@ModelAttribute("userFood") FoodDTO food) {

        foodService.deleteByNameAndUserId(food.getName(), ContextUtils.getPrincipal());
        return "redirect:/user/main";
    }

    @GetMapping("/history")
    public String getHistory(Model model) {
        User user = ContextUtils.getPrincipal();

        addDaysAndStatsModelAttributes(model, user);
        model.addAttribute("userName", user.getUsername());
        model.addAttribute("dailyNorm", user.getDailyNormCalories());
        return "user/history";
    }

    private void addDaysAndStatsModelAttributes(Model model, User user) {
        model.addAttribute("daysOfUser", dayService.getAllDaysByUser(user));
    }

    @GetMapping("/profile")
    public String getProfile(Model model) {
        log.debug("Getting profile page");

        UserDTO userDTO = DTOConverter.userToUserDTO(ContextUtils.getPrincipal());
//        try {
//            userDTO = userService.getUserById(ContextUtils.getPrincipal().getId());
//        } catch (UserNotFoundException e) {
//            log.error("User with not found ",  e);
//        }
        log.debug("Got userDTO from context with id {}", userDTO.getId());
        model.addAttribute("userDTO", userDTO);

        return "user/profile";
    }

    @PostMapping("/update")
    public String update(
            @Valid UserDTO userDTO,
            BindingResult bindingResult) {

        User currentUser = ContextUtils.getPrincipal();

        if (bindingResult.hasErrors()) {
            log.warn("Errors in input: {}", bindingResult.getAllErrors());
            log.warn(" Redirecting back to profile");
            return "user/profile";
        }

        updateUser(currentUser, userDTO);

        log.debug("updating biometrics");
        biometricService.update(currentUser.getBiometrics());

        log.debug("Updating user");
        userService.update(currentUser);
        return "redirect:/user/profile";
    }

    private void updateUser(User user, UserDTO userDTO) {
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());

        Biometrics biometrics = user.getBiometrics();

        biometrics.setAge(userDTO.getAge());
        biometrics.setHeight(userDTO.getHeight());
        biometrics.setWeight(userDTO.getWeight());
        biometrics.setSex(userDTO.getSex());
        biometrics.setLifestyle(userDTO.getLifestyle());
    }
}
