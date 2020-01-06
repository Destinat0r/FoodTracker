package all.that.matters.controller;

import all.that.matters.domain.Food;
import all.that.matters.domain.Role;
import all.that.matters.domain.User;
import all.that.matters.services.FoodService;
import all.that.matters.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    private UserService userService;

    private FoodService foodService;

    @Autowired
    public MainController(UserService userService, FoodService foodService) {
        this.userService = userService;
        this.foodService = foodService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("user", new User());
        return "index";
    }

    @GetMapping("/main")
    public String login(Model model ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = (User) userDetails;

        if (user.getRoles().contains(Role.ADMIN)) {
            return "admin_main";
        }

        List<Food> usersFood = foodService.findAllFoodByOwner(user);

        model.addAttribute("user", user);
        model.addAttribute("usersFood", usersFood);

        return "main";
    }

    @GetMapping("/login")
    public String loginForm() {
        return "login";
    }


    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }
}
