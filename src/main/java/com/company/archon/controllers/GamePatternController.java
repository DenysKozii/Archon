package com.company.archon.controllers;

import com.company.archon.dto.GamePatternDto;
import com.company.archon.entity.GamePattern;
import com.company.archon.entity.User;
import com.company.archon.services.GamePatternService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/gamePattern")
public class GamePatternController {

    private final GamePatternService gamePatternService;

    @Autowired
    public GamePatternController(GamePatternService gamePatternService) {
        this.gamePatternService = gamePatternService;
    }


    @PostMapping("/new")
    public String newGamePattern(@RequestParam String title,
                                 @RequestParam Integer usersAmount,
                                 @AuthenticationPrincipal User user, Model model) {
        GamePatternDto gamePatternDto = gamePatternService.createGamePattern(title, usersAmount, user);
        List<GamePatternDto> games = gamePatternService.getGamePatternsByUser(user);
        model.addAttribute("games",games);
        return "redirect:/parameter/list/"+gamePatternDto.getId();
    }

    @GetMapping("/list")
    public String gamePatternsList(@AuthenticationPrincipal User user, Model model) {
        List<GamePatternDto> games = gamePatternService.getGamePatternsByUser(user);
        model.addAttribute("games",games);
        return "gamePatternList";
    }

    @GetMapping("/list/{userEmail}")
    public String gamePatternsList(@PathVariable String userEmail, @AuthenticationPrincipal User user, Model model) {
        List<GamePatternDto> games = gamePatternService.getGamePatternsByUser(user, userEmail);
        model.addAttribute("games",games);
        model.addAttribute("friendEmail", userEmail);
        return "gamePatternList";
    }

    @PostMapping("/delete/{gamePatternId}")
    public String deleteQuestion(@PathVariable Long gamePatternId, @AuthenticationPrincipal User user, Model model) {
        gamePatternService.deleteGamePattern(gamePatternId);
        return "redirect:/gamePattern/list";
    }
}
