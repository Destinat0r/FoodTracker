package all.that.matters.controller;

import all.that.matters.dto.ConsumedStatsDto;
import all.that.matters.dto.EventDTO;
import all.that.matters.dto.FoodDTO;
import all.that.matters.dto.UserDto;
import all.that.matters.model.User;
import all.that.matters.services.EventService;
import all.that.matters.services.FoodService;
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

    @Autowired
    public void setFoodService(FoodService foodService) {
        this.foodService = foodService;
    }

    @Autowired
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/main")
    public String getMain(Model model ) {

        User user = ControllerUtils.getPrincipal();
        UserDto userDto = UserDto.builder()
                                  .username(user.getUsername())
                                  .dailyNorm(user.getBiometrics().getDailyNorm())
                                  .build();

        ConsumedStatsDto consumedStatsDto = foodService.getConsumedStatsForUserAndDate(user, LocalDate.now());

        model.addAttribute("allCommonFood", foodService.findAllCommonFoodInDtos());
        model.addAttribute("food", new FoodDTO());
        model.addAttribute("usersFoodDtos", foodService.findAllByOwner(user));
        model.addAttribute("consumedStatsDto", consumedStatsDto);
        model.addAttribute("userDto", userDto);
        model.addAttribute("todayEventsDtos", eventService.findForToday());

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

    @PostMapping("/consume")
    public String consume(
            @ModelAttribute("food") FoodDTO food,
            @RequestParam("amount") BigDecimal amount,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
        }

        foodService.registerConsumption(food);

        return "redirect:/user/main";
    }

    @GetMapping("/history")
    public String getHistory(Model model) {

        User user = ControllerUtils.getPrincipal();
        Map<LocalDate, List<EventDTO>> dateToEventDtosMap = eventService.getDayToEventDtosMapByUserId(user.getId());

        model.addAttribute("dateToEventDtosMap", dateToEventDtosMap);
        model.addAttribute("userName", user.getUsername());
        return "history";
    }
}
