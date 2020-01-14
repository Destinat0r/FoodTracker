package all.that.matters.controller;

import all.that.matters.model.Role;
import all.that.matters.model.User;
import all.that.matters.repo.EventRepo;
import all.that.matters.services.UserService;
import all.that.matters.utils.ContextUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    private UserService userService;
    private EventRepo eventRepo;

    @Autowired
    public LoginController(UserService userService, EventRepo eventRepo) {
        this.userService = userService;
        this.eventRepo = eventRepo;
    }

    @GetMapping("/")
    public String getIndex(Model model) {
        return "index";
    }

    @GetMapping("/redirect")
    public String getRedirect(Model model) {
        model.addAttribute("user", new User());

        User user = ContextUtils.getPrincipal();

        if (user.getRoles().contains(Role.ADMIN)) {
            return "redirect:/admin/main";
        }
            return "redirect:/user/main";
    }

    @GetMapping("/login")
    public String getLoginForm() {
        return "login";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String getAdminMain() {
        return "admin/main";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/users";
    }
}
