package all.that.matters.controller;

import all.that.matters.model.*;
import all.that.matters.dto.ConsumedStatsDto;
import all.that.matters.dto.FoodDto;
import all.that.matters.dto.UserDto;
import all.that.matters.services.EventService;
import all.that.matters.services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user/food")
public class UserFoodController {

    @Autowired
    private FoodService foodService;

    @Autowired
    private EventService eventService;


    @GetMapping("/main")
    public String getMain(Model model ) {

        User user = ControllerUtils.getPrincipal();
        UserDto userDto = UserDto.builder()
                                  .username(user.getUsername())
                                  .dailyNorm(user.getBiometrics().getDailyNorm())
                                  .build();

        ConsumedStatsDto consumedStatsDto = foodService.getConsumedStatsForUserAndDate(user, LocalDate.now());

        model.addAttribute("allCommonFood", foodService.findAllCommonFoodInDtos());
        model.addAttribute("food", new FoodDto());
        model.addAttribute("usersFoodDtos", foodService.findAllByOwner(user));
        model.addAttribute("consumedStatsDto", consumedStatsDto);
        model.addAttribute("userDto", userDto);
        model.addAttribute("todayEventsDtos", eventService.findForToday());

        return "user_main";
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getAllFoods(Model model) {

        List<FoodDto> allFood = foodService.findAllCommonFoodInDtos();
        model.addAttribute("allFood", allFood);

        return "all_food";
    }

    @PostMapping("/add")
    public String add(
            @Valid FoodDto userFoodDto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
        }

        foodService.add(userFoodDto);
        return "redirect:/user/food/main";
    }

    @PostMapping("/consume")
    public String consume(
            @ModelAttribute("food") FoodDto food,
            @RequestParam("amount") Double amount,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
        }

        foodService.registerConsumption(food);

        return "redirect:/user/food/main";
    }



}
