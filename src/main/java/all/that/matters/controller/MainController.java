package all.that.matters.controller;

import all.that.matters.domain.Role;
import all.that.matters.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    @GetMapping("/login")
    public String login(User user) {
        if (user.getRoles().contains(Role.ADMIN)) {
            return "users";
        }

        return "main";
    }
}
