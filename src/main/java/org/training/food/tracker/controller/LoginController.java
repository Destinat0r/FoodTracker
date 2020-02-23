package org.training.food.tracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.training.food.tracker.model.Role;
import org.training.food.tracker.model.User;
import org.training.food.tracker.services.UserService;
import org.training.food.tracker.services.defaults.UserServiceDefault;
import org.training.food.tracker.utils.ContextUtils;

@Controller
public class LoginController {

    private UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/")
    public String getIndex(Model model) {
        User user = ContextUtils.getPrincipal();

        if (user.getRole() == Role.ADMIN) {
            return "admin/index";
        } else
            return "user/index";
    }

    @GetMapping("/redirect")
    public String getRedirect(Model model) {
        model.addAttribute("user", new User());

        User user = ContextUtils.getPrincipal();

        if (user.getRole() == Role.ADMIN) {
            return "redirect:/admin/main";
        }
            return "redirect:/user/main";
    }

    @GetMapping("/login")
    public String getLoginForm() {
        return "login";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.update(user);
        return "redirect:/users";
    }
}
