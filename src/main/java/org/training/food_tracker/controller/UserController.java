package org.training.food_tracker.controller;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.training.food_tracker.dto.FoodDTO;
import org.training.food_tracker.dto.UserDTO;
import org.training.food_tracker.model.Biometrics;
import org.training.food_tracker.model.Day;
import org.training.food_tracker.model.User;
import org.training.food_tracker.repo.exceptions.FoodNotFoundException;
import org.training.food_tracker.repo.exceptions.UserNotFoundException;
import org.training.food_tracker.services.*;
import org.training.food_tracker.utils.ContextUtils;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Slf4j
@Log4j2

@Controller
@RequestMapping("/user")
public class UserController {

    private FoodService foodService;
    private DayService dayService;
    private UserService userService;
    private BiometricService biometricService;

    @Autowired
    public void setFoodService(FoodService foodService) {
        this.foodService = foodService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setDayService(DayService dayService) {
        this.dayService = dayService;
    }

    @Autowired
    public void setBiometricService(BiometricService biometricService) {
        this.biometricService = biometricService;
    }

    @GetMapping("/main")
    public String getMain(Model model) {
        log.debug("loading user's main");

        User user = ContextUtils.getPrincipal();
        UserDTO userDTO = userService.getCurrentUserDTO();

        log.debug("setting allCommonFood");
        model.addAttribute("allCommonFood", foodService.findAllCommonExcludingPersonalByUserIdInDTO(user.getId()));

        model.addAttribute("food", new FoodDTO());

        log.debug("setting usersFoodDTOs");
        model.addAttribute("usersFoodDTOs", foodService.findAllByOwnerInDTOs(user));

        log.debug("setting userDTO {}", userDTO);
        model.addAttribute("userDTO", userDTO);

        log.debug("getting current day");
        Day currentDay = dayService.getCurrentDayOfUser(user);

        model.addAttribute("currentDay", currentDay);

        log.debug("getting consumedStatsDTO");
        model.addAttribute("consumedStatsDTO",
                dayService.getDayStatsForUser(user, currentDay));

        return "user/main";
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getAllFoods(Model model) {

        List<FoodDTO> allFood = foodService.findAllCommonInDtos();
        model.addAttribute("allFood", allFood);

        return "food_list";
    }

    @PostMapping("/add")
    public String add(
            @Valid FoodDTO userFoodDTO,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
        }

        foodService.add(userFoodDTO);
        return "redirect:/user/main";
    }

    @PostMapping(value = "/use", params = "consume")
    public String consume(
            @Valid FoodDTO foodDTO,
            BindingResult bindingResult,
            Model model) {

        log.debug("Obtained foodDTO from client: {} ", foodDTO);

        if (bindingResult.hasErrors()) {
            return "main";
        }

        log.debug("Registering consumption");
        foodService.registerConsumption(foodDTO);

        return "redirect:/user/main";
    }

    @PostMapping(value = "/use", params = "delete")
    public String delete(@ModelAttribute("userFood") FoodDTO food) {

        foodService.removeByNameAndUserId(food.getName(), ContextUtils.getPrincipal());
        return "redirect:/user/main";
    }

//    @GetMapping("/history")
//    public String getHistory(Model model) {
//
//        User user = ControllerUtils.getPrincipal();
//        List<EventDTOsPack> eventDTOsPacks = consumedFoodService.getEventDTOsPacksByUserId(user.getId());
//
//        model.addAttribute("eventDTOsPacks", eventDTOsPacks);
//        model.addAttribute("userName", user.getUsername());
//        return "user/history";
//    }

    @GetMapping("/profile")
    public String getProfile(Model model) {
        log.debug("Getting profile page");

        UserDTO userDTO = null;
        try {
            userDTO = userService.getUserDTOById(ContextUtils.getPrincipal().getId());
        } catch (UserNotFoundException e) {
            log.error("User with not found ",  e);
        }
        log.debug("Got userDTO from context with id {}", userDTO.getId());
        model.addAttribute("userDTO", userDTO);

        return "user/profile";
    }

    @PostMapping("/update")
    public String update(
            @Valid UserDTO userDTO,
            BindingResult bindingResult) {

        User currentUser = ContextUtils.getPrincipal();

        Long userId = currentUser.getId();

        userDTO.setId(userId);
        log.debug("Updating user {} with id {}", userDTO, userId);

        if (bindingResult.hasErrors()) {
            log.warn("Errors in input: {}", bindingResult.getAllErrors());
            log.warn(" Redirecting back to profile");
            return "user/profile";
        }

        log.debug("updating biometrics");
        biometricService.update(userDTO);

        updateUser(currentUser, userDTO);

        log.debug("Updating user");
        userService.update(userDTO);
        return "redirect:/user/profile";
    }

    private void updateUser(User user, UserDTO userDTO) {
        user.setEmail(userDTO.getEmail());
        user.setFullName(userDTO.getFullName());
        user.setNationalName(userDTO.getNationalName());

        Biometrics biometrics = user.getBiometrics();

        biometrics.setAge(userDTO.getAge());
        biometrics.setHeight(userDTO.getHeight());
        biometrics.setWeight(userDTO.getWeight());
        biometrics.setSex(userDTO.getSex());
        biometrics.setLifestyle(userDTO.getLifestyle());
        biometrics.setDailyNorm();
    }
}
