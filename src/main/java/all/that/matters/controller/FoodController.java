package all.that.matters.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/all_foods")
public class FoodController {

    @GetMapping("/")
    public String getAllFoods(Model model) {

        return "all_foods";
    }
}
