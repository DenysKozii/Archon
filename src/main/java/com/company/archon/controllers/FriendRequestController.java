package com.company.archon.controllers;


import com.company.archon.entity.User;
import com.company.archon.services.FriendRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@Slf4j
@CrossOrigin(origins = "*")
@RequestMapping("/friends")
public class FriendRequestController {

    private final FriendRequestService friendRequestService;

    @Autowired
    public FriendRequestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }


    @GetMapping("/accept/list")
    public String acceptList(@AuthenticationPrincipal User user, Model model) {
        List<String> acceptList = friendRequestService.acceptList(user.getEmail());
        addCommonAttributes(user, acceptList, model);
        return "profile";
    }

    @GetMapping("/invite/list")
    public String inviteList(@AuthenticationPrincipal User user, Model model) {
        List<String> inviteList = friendRequestService.inviteList(user.getEmail());
        addCommonAttributes(user, inviteList, model);
        return "profile";
    }

    @PostMapping("/invite")
    public String inviteFriend(@RequestParam String email, @AuthenticationPrincipal User user, Model model) {
        friendRequestService.inviteByEmail(user.getEmail(), email);
        return "redirect:/friends/invite/list";
    }


    @PostMapping("/accept")
    public String acceptFriend(@RequestParam String email, @AuthenticationPrincipal User user, Model model) {
        friendRequestService.acceptByEmail(user.getEmail(), email);
        return "redirect:/friends/accept/list";
    }


    @DeleteMapping("/delete/{userEmail}")
    public String deleteFriend(@PathVariable String userEmail, @AuthenticationPrincipal User user, Model model) {
        friendRequestService.deleteByEmail(user.getEmail(), userEmail);
        return "redirect:profile";
    }

    private void addCommonAttributes(User user, List<String> friends, Model model) {
        model.addAttribute("username", user.getUsername())
                .addAttribute("friends", friends)
                .addAttribute("main", false)
                .addAttribute("email", user.getEmail());
    }
}