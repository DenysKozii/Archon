package com.company.archon.controllers;

import com.company.archon.dto.UserDto;
import com.company.archon.entity.FriendRequest;
import com.company.archon.entity.User;
import com.company.archon.services.UserService;
import liquibase.pro.packaged.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class MainController {

    private final UserService userService;

    @Autowired
    public MainController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = {"/profile", "/"})
    public String profile(@AuthenticationPrincipal User user, Model model) {
        List<String> friends = userService.getFriendsByUserEmail(user.getEmail());
        addCommonAttributes(user, model);
        model.addAttribute("friends", friends);
        model.addAttribute("main", true);
        return "profile";
    }

    private void addCommonAttributes(User user, Model model) {
        model.addAttribute("username", user.getUsername())
                .addAttribute("email", user.getEmail());
    }
}
