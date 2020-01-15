package all.that.matters.controller;

import all.that.matters.dto.EventDTOsPack;
import all.that.matters.dto.FoodDTO;
import all.that.matters.dto.UserDTO;
import all.that.matters.model.User;
import all.that.matters.repo.FoodNotFoundException;
import all.that.matters.repo.UserNotFoundException;
import all.that.matters.services.EventService;
import all.that.matters.services.FoodService;
import all.that.matters.services.UserService;
import all.that.matters.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {

    private FoodService foodService;
    private EventService eventService;
    private UserService userService;

    @Autowired
    public void setFoodService(FoodService foodService) {
        this.foodService = foodService;
    }

    @Autowired
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/main")
    public String getMain(Model model ) {

        User user = ContextUtils.getPrincipal();
        UserDTO userDTO = userService.getCurrentUserDTO();

        model.addAttribute("allCommonFood",
                foodService.findAllCommonFoodExcludingPersonalByUserIdInDTO(user.getId()));

        model.addAttribute("food", new FoodDTO());
        model.addAttribute("usersFoodDTOs", foodService.findAllByOwner(user));

        model.addAttribute("consumedStatsDTO",
                foodService.getConsumedStatsForUserAndDate(user, LocalDate.now()));

        model.addAttribute("userDTO", userDTO);
        model.addAttribute("todayEventsDTOs", eventService.findForToday());

        return "user/main";
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getAllFoods(Model model) {

        List<FoodDTO> allFood = foodService.findAllCommonFoodInDtos();
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
            @ModelAttribute("food") FoodDTO food,
            @RequestParam("amount") BigDecimal amount,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
        }

        try {
            foodService.registerConsumption(food);
        } catch (FoodNotFoundException e) {
            return "errors/food_not_found";
        }

        return "redirect:/user/main";
    }

    @PostMapping(value = "/use", params = "delete")
    public String delete(@ModelAttribute("userFood") FoodDTO food) {

        foodService.removeByFoodNameAndUserId(food.getName(), ContextUtils.getPrincipal());
        return "redirect:/user/main";
    }

    @GetMapping("/history")
    public String getHistory(Model model) {

        User user = ControllerUtils.getPrincipal();
        List<EventDTOsPack> eventDTOsPacks = eventService.getEventDTOsPacksByUserId(user.getId());

        model.addAttribute("eventDTOsPacks", eventDTOsPacks);
        model.addAttribute("userName", user.getUsername());
        return "user/history";
    }

    @GetMapping("/profile")
    public String getProfile(Model model) {
        try {
            model.addAttribute("userDTO", userService.getUserDTOById(ContextUtils.getPrincipal().getId()));
        } catch (UserNotFoundException e) {
            return "errors/no_such_user";
        }
        return "user/profile";
    }
}
