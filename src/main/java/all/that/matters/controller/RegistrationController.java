package all.that.matters.controller;

import all.that.matters.dto.UserDTO;
import all.that.matters.model.Biometrics;
import all.that.matters.model.User;
import all.that.matters.repo.UserExistsException;
import all.that.matters.services.BiometricService;
import all.that.matters.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

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
        model.addAttribute("userDTO", new UserDTO());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
            @RequestParam("passwordConfirm") String passwordConfirm,
            @Valid UserDTO userDTO,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        if (!userDTO.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordsDontMatch", true);
            return "registration";
        }

        User user = userService.userDTOtoUser(userDTO);
        Biometrics biometrics = biometricService.userDTOtoBiometrics(userDTO);

        biometrics.setDailyNorm();

        try {
            biometricService.create(biometrics);
            user.setBiometrics(biometrics);
            biometrics.setOwner(user);
            userService.create(user);
        } catch (UserExistsException e) {
            model.addAttribute("usernameError", "Such username or email already exists!");
            return "registration";
        }

        return "redirect:/login";
    }
}
