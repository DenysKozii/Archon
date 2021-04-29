package com.company.archon.controllers;

import com.company.archon.dto.ParameterDto;
import com.company.archon.dto.UserDto;
import com.company.archon.dto.UserParameterDto;
import com.company.archon.entity.Parameter;
import com.company.archon.services.AuthorizationService;
import com.company.archon.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@Controller
@AllArgsConstructor
public class UserController {
    private final AuthorizationService authorizationService;
    private final UserService userService;

    @PostMapping("/")
    public String addUser(@Valid UserDto user) {
        userService.addUser(user);
        return "redirect:/profile";
    }

    @PostMapping("/update/{parameterId}")
    public String updateParameter(@PathVariable Long parameterId, @RequestParam Integer value) {
        userService.updateParameter(parameterId, value);
        return "redirect:/profile";
    }

    @GetMapping("/")
    public String login() {
        return "login";
    }

    @GetMapping(value = {"/profile"})
    public String profile(Model model) {
        String username = authorizationService.getProfileOfCurrent().getUsername();
        List<UserParameterDto> parameters = userService.findParameters(username);
        model.addAttribute("username", username);
        model.addAttribute("parameters", parameters);
        return "profile";
    }
}
