package org.training.food_tracker.controller;

import org.training.food_tracker.model.Role;
import org.training.food_tracker.model.User;
import org.training.food_tracker.repo.EventRepo;
import org.training.food_tracker.services.UserService;
import org.training.food_tracker.utils.ContextUtils;
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
        userService.save(user);
        return "redirect:/users";
    }
}
