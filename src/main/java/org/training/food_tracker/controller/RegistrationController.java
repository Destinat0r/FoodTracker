package org.training.food_tracker.controller;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.training.food_tracker.dto.UserDTO;
import org.training.food_tracker.model.Biometrics;
import org.training.food_tracker.model.User;
import org.training.food_tracker.repo.exceptions.UserExistsException;
import org.training.food_tracker.services.defaults.BiometricServiceDefault;
import org.training.food_tracker.services.defaults.UserServiceDefault;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@Slf4j
@Log4j2

@Controller
public class RegistrationController {

    private UserServiceDefault userServiceDefault;
    private BiometricServiceDefault biometricServiceDefault;

    @Autowired
    public RegistrationController(UserServiceDefault userServiceDefault, BiometricServiceDefault biometricServiceDefault) {
        this.userServiceDefault = userServiceDefault;
        this.biometricServiceDefault = biometricServiceDefault;
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

        User user = userServiceDefault.userDTOtoUser(userDTO);
        Biometrics biometrics = biometricServiceDefault.userDTOtoBiometrics(userDTO);

        biometrics.setDailyNorm();

        try {
            log.debug("Creating and setting biometrics to user");
            biometricServiceDefault.create(biometrics);
            user.setBiometrics(biometrics);
            biometrics.setOwner(user);

            log.debug("Creating user");
            userServiceDefault.create(user);
        } catch (UserExistsException e) {
            model.addAttribute("usernameError", "Such username or email already exists!");
            log.error("Failed to create the user", e);
            return "registration";
        }

        return "redirect:/login";
    }
}
