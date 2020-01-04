package all.that.matters.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class RegistrationController {

    @GetMapping("/registration")
    public String registration(@ModelAttribute("message") String message) {
        return "registration";
    }
}
