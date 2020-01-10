package all.that.matters.controller;

import all.that.matters.dao.EventRepository;
import all.that.matters.domain.User;
import all.that.matters.dto.EventDto;
import all.that.matters.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class UserController {

    private UserService userService;
    private EventRepository eventRepository;

    @Autowired
    public UserController(UserService userService, EventRepository eventRepository) {
        this.userService = userService;
        this.eventRepository = eventRepository;
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
    @GetMapping("/user")
    public String getUserProfile(@RequestParam Long id, Model model) {
        Optional<User> user = userService.findById(id);
        user.ifPresent((user1) -> model.addAttribute("user", user1));
        return "profile";
    }

    @PostMapping("/update")
    public String updateUser(@ModelAttribute("user") User user) {
        userService.save(user);
        return "redirect:/users";
    }

    @GetMapping("/user/history")
    public String getHistory(Model model) {

        User user = ControllerUtils.getPrincipal();
        EventDto eventDto = new EventDto(eventRepository.findAllByUserId(user.getId()), user);

        model.addAttribute("eventsDto", eventDto);
        return "history";
    }
}
