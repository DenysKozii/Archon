package com.company.archon.controllers.game;

import com.company.archon.dto.GamePatternDto;
import com.company.archon.entity.GamePattern;
import com.company.archon.entity.User;
import com.company.archon.pagination.PageDto;
import com.company.archon.services.GamePatternService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@AllArgsConstructor
@RequestMapping("/gamePattern")
public class GamePatternController {

    private final GamePatternService gamePatternService;


    @PostMapping("/new")
    public String newGamePattern(@RequestParam(defaultValue = "Test") String title,
                                 Model model) {
        GamePatternDto gamePatternDto = gamePatternService.createGamePattern(title);
        PageDto<GamePatternDto> games = gamePatternService.getGamePatterns(0, 150);
        model.addAttribute("games",games.getObjects());
        return "redirect:/parameter/list/"+gamePatternDto.getId();
    }

    @GetMapping("/list")
    public String gamePatternsList(Model model) {
        PageDto<GamePatternDto> games = gamePatternService.getGamePatterns(0, 150);
        model.addAttribute("games",games.getObjects());
        return "gamePatternList";
    }

    @PostMapping("/delete/{gamePatternId}")
    public String delete(@PathVariable Long gamePatternId) {
        gamePatternService.deleteById(gamePatternId);
        return "redirect:/gamePattern/list";
    }
}
