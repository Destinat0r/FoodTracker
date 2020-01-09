package all.that.matters.controller;

import all.that.matters.domain.User;
import all.that.matters.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    private UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String getIndex(Model model) {
        model.addAttribute("user", new User());
        return "index";
    }

    @GetMapping("/login")
    public String getLoginForm() {
        return "login";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getAdminMain() {
        return "admin_main";
    }

    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("users", userService.findAll());
        return "users";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/users/{id}")
    public String getUserProfile(@PathVariable Long id, Model model) {
        Optional<User> user = userService.findById(id);
        user.ifPresent((user1) -> model.addAttribute("user", user1));
        return "profile";
    }
}
