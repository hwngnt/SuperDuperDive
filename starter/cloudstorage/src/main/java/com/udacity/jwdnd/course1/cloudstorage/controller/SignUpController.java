package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class SignUpController {
    private final UserService userService;

    public SignUpController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/signup")
    public String getSignUp(){
        return "signup";
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute User user, Model model){
        String error = null;

        if (userService.isUserNameAvailable(user.getUsername())) {
            int numberOfUserAdded = userService.createUser(user);
        } else {
            error = "The username already exists.";
        }
        if (error == null) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("error", error);
        }
        return "signup";
    }
}
