package org.training.food.tracker.controller;

import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.training.food.tracker.dto.BiometricsDTO;
import org.training.food.tracker.dto.DTOConverter;
import org.training.food.tracker.dto.UserDTO;
import org.training.food.tracker.model.Biometrics;
import org.training.food.tracker.model.User;
import org.training.food.tracker.repo.exceptions.UserExistsException;
import org.training.food.tracker.services.BiometricService;
import org.training.food.tracker.services.UserService;

import javax.validation.Valid;

@Slf4j
@Log4j2

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
            BindingResult userBindingResult,
            @Valid BiometricsDTO biometricsDTO,
            BindingResult biometricsBindingResult,
            Model model) {

        if (userBindingResult.hasErrors() || biometricsBindingResult.hasErrors()) {
            return "registration";
        }

        if (!userDTO.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordsDontMatch", true);
            return "registration";
        }

        User user = DTOConverter.userDTOtoUser(userDTO);
        Biometrics biometrics = DTOConverter.biometricsDTOtoBiometrics(biometricsDTO);
        user.setDailyNormCalories(userService.calculateDailyNormCalories(biometrics));

        try {
            log.debug("Creating and setting biometrics to user");
            biometricService.create(biometrics);
            user.setBiometrics(biometrics);
            biometrics.setOwner(user);

            log.debug("Creating user");
            userService.create(user);
        } catch (UserExistsException e) {
            model.addAttribute("usernameError", "Such username or email already exists!");
            log.error("Failed to create the user", e);
            return "registration";
        }

        return "redirect:/login";
    }
}
