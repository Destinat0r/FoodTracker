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
import org.training.food.tracker.dto.FoodDTO;
import org.training.food.tracker.dto.UserDTO;
import org.training.food.tracker.model.Biometrics;
import org.training.food.tracker.model.Day;
import org.training.food.tracker.model.User;
import org.training.food.tracker.repo.exceptions.UserNotFoundException;
import org.training.food.tracker.services.defaults.BiometricServiceDefault;
import org.training.food.tracker.services.defaults.DayServiceDefault;
import org.training.food.tracker.services.defaults.FoodServiceDefault;
import org.training.food.tracker.services.defaults.UserServiceDefault;
import org.training.food.tracker.utils.ContextUtils;

import javax.validation.Valid;
import java.util.List;

@Slf4j
@Log4j2

@Controller
@RequestMapping("/user")
public class UserController {

    private FoodServiceDefault foodServiceDefault;
    private DayServiceDefault dayServiceDefault;
    private UserServiceDefault userServiceDefault;
    private BiometricServiceDefault biometricServiceDefault;

    @Autowired
    public void setFoodServiceDefault(FoodServiceDefault foodServiceDefault) {
        this.foodServiceDefault = foodServiceDefault;
    }

    @Autowired
    public void setUserServiceDefault(UserServiceDefault userServiceDefault) {
        this.userServiceDefault = userServiceDefault;
    }

    @Autowired
    public void setDayServiceDefault(DayServiceDefault dayServiceDefault) {
        this.dayServiceDefault = dayServiceDefault;
    }

    @Autowired
    public void setBiometricServiceDefault(BiometricServiceDefault biometricServiceDefault) {
        this.biometricServiceDefault = biometricServiceDefault;
    }

    @GetMapping("/main")
    public String getMain(Model model) {
        log.debug("loading user's main");

        User user = ContextUtils.getPrincipal();
        UserDTO userDTO = userServiceDefault.getCurrentUserDTO();

        log.debug("setting allCommonFood");
        model.addAttribute("allCommonFood", foodServiceDefault.findAllCommonExcludingPersonalByUserIdInDTO(user.getId()));

        model.addAttribute("food", new FoodDTO());

        log.debug("setting usersFoodDTOs");
        model.addAttribute("usersFoodDTOs", foodServiceDefault.findAllByOwnerInDTOs(user));

        log.debug("setting userDTO {}", userDTO);
        model.addAttribute("userDTO", userDTO);

        log.debug("getting current day");
        Day currentDay = dayServiceDefault.getCurrentDayOfUser(user);

        model.addAttribute("currentDay", currentDay);

        log.debug("getting consumedStatsDTO");
        model.addAttribute("consumedStatsDTO", dayServiceDefault.getConsumeStatsForDay(currentDay));

        return "user/main";
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getAllFoods(Model model) {

        List<FoodDTO> allFood = foodServiceDefault.findAllCommonInDtos();
        model.addAttribute("allFood", allFood);

        return "food_list";
    }

    @PostMapping("/add")
    public String add(FoodDTO userFoodDTO) {
        foodServiceDefault.add(userFoodDTO);
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

        log.debug("Registering consumption");
        foodServiceDefault.registerConsumption(foodDTO);

        return "redirect:/user/main";
    }

    @PostMapping(value = "/use", params = "delete")
    public String delete(@ModelAttribute("userFood") FoodDTO food) {

        foodServiceDefault.removeByNameAndUserId(food.getName(), ContextUtils.getPrincipal());
        return "redirect:/user/main";
    }

    @GetMapping("/history")
    public String getHistory(Model model) {
        User user = ContextUtils.getPrincipal();

        addDaysAndStatsModelAttributes(model, user);
        model.addAttribute("userName", user.getUsername());
        model.addAttribute("dailyNorm", user.getBiometrics().getDailyNorm());
        return "user/history";
    }

    @Transactional void addDaysAndStatsModelAttributes(Model model, User user) {
        model.addAttribute("daysAndStats" , dayServiceDefault.getDaysToConsumeStatsForUser(user));
        model.addAttribute("daysOfUser", dayServiceDefault.getAllDaysByUser(user));
    }

    @GetMapping("/profile")
    public String getProfile(Model model) {
        log.debug("Getting profile page");

        UserDTO userDTO = null;
        try {
            userDTO = userServiceDefault.getUserDTOById(ContextUtils.getPrincipal().getId());
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

        updateUser(currentUser, userDTO);

        log.debug("updating biometrics");
        biometricServiceDefault.update(userDTO);

        log.debug("Updating user");
        userServiceDefault.update(userDTO);
        return "redirect:/user/profile";
    }

    private void updateUser(User user, UserDTO userDTO) {
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
