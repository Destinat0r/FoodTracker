package all.that.matters.controller;

import all.that.matters.domain.Food;
import all.that.matters.services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/all_foods")
public class FoodController {

    private FoodService foodService;

    @Autowired
    public FoodController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping("/")
    public String getAllFoods(Model model) {

        List<Food> allFood = foodService.findAll();
        model.addAttribute("allFood", allFood);

        return "all_foods";
    }
}
