package all.that.matters.controller;

import all.that.matters.domain.Food;
import all.that.matters.services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/food")
public class FoodController {

    private FoodService foodService;

    @Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getAllFoods(Model model) {

        List<Food> allFood = foodService.findAll();
        model.addAttribute("allFood", allFood);

        return "all_food";
    }

    @PostMapping("/add_to_all")
    public String addToAll(
            @Valid Food food,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            Map<String, String> errors = ControllerUtils.getErrors(bindingResult);
            model.mergeAttributes(errors);
        }

        foodService.add(food);

        return "redirect:/food/all";
    }
}
