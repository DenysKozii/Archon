package com.company.archon.controllers;

import com.company.archon.entity.User;
import com.company.archon.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegisterController {

    @Autowired
    UserService userService;

    @PostMapping("/registration")
    public String addUser(@Valid User user, Errors errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("error", true);
            return "registration";
        }
        if (!user.getPassword().equals(user.getConfirmPassword())) {
            model.addAttribute("message", "Check your password input");
            return "registration";
        }
        boolean addUser = userService.addUser(user);
        if (addUser) {
            return "redirect:/login";
        }
        model.addAttribute("message", "User already exists!");
        return "login";
    }

    @GetMapping("/registration")
    public String register() {
        return "registration";
    }

}
