package all.that.matters.controller;

import all.that.matters.model.Biometrics;
import all.that.matters.model.User;
import all.that.matters.services.BiometricService;
import all.that.matters.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {

    private UserService userService;
    private BiometricService biometricService;

    @Autowired
    public RegistrationController(UserService userService, BiometricService biometricService) {
        this.userService = userService;
        this.biometricService = biometricService;
    }

    @GetMapping("/registration")
    public String registration(@ModelAttribute("message") String message, Model model) {
        model.addAttribute("biometrics", new Biometrics());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("passwordConfirm") String passwordConfirm,
            @Valid @ModelAttribute("user") User user,
            Biometrics biometrics,
            BindingResult bindingResult,
            Model model) {


        if (bindingResult.hasErrors()) {
            bindingResult.rejectValue("username", "Error!!");

            return "registration";
        }

        biometrics.setDailyNorm();

        try {
            biometricService.create(biometrics);
            user.setBiometrics(biometrics);
            biometrics.setOwner(user);
            userService.addUser(user);
        } catch (Exception e) {
            model.addAttribute("usernameError", "Username or email already exists!");
            model.addAttribute("emailError", "Username or email already exists!");
            return "registration";
        }

        return "redirect:/login";
    }
}
