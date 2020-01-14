package all.that.matters.controller;

import all.that.matters.dto.FoodDto;
import all.that.matters.services.FoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private FoodService foodService;

    @Autowired
    public AdminController(FoodService foodService) {
        this.foodService = foodService;
    }

    @GetMapping("/main")
    public String getMain(Model model) {
        return "admin_main";
    }

    @GetMapping("/food_list")
    public String getFoodList(Model model) {
        List<FoodDto> allFood = foodService.findAllCommonFoodInDtos();
        model.addAttribute("allFood", allFood);
        return "food_list";
    }
}
