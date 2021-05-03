package com.company.archon.controllers.game;

import com.company.archon.dto.GameDto;
import com.company.archon.enums.GameStatus;
import com.company.archon.pagination.PageDto;
import com.company.archon.services.GameService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
@RequestMapping("/game")
public class GameController {
    private final GameService gameService;

    @GetMapping("/new/{gamePatternId}")
    public String newGame(@PathVariable Long gamePatternId, Model model) {
        GameDto game = gameService.startNewGame(gamePatternId);
        model.addAttribute("game", game);
        return "game";
    }

    @PostMapping("/answer/{gameId}/{answerId}")
    public String checkAnswer(@PathVariable Long gameId,
                              @PathVariable Long answerId, Model model) {
        GameDto game = gameService.answerInfluence(answerId, gameId);
        model.addAttribute("game", game);
        if (GameStatus.GAME_OVER.equals(game.getGameStatus())){
            gameService.deleteById(gameId);
            return "gameOver";
        }
        if (GameStatus.COMPLETED.equals(game.getGameStatus())){
            gameService.deleteById(gameId);
            return "gameCompleted";
        }
        if (game.getQuestion() == null){
            return "noQuestion";
        }
        return "game";
    }

    @GetMapping("/save/{gameId}")
    public String saveGame(@PathVariable Long gameId) {
        gameService.saveGame(gameId);
        return "redirect:/gamePattern/list";
    }

    @GetMapping("/saved")
    public String savedGames(Model model) {
        PageDto<GameDto> savedGames = gameService.savedGames(0, 150);
        model.addAttribute("games", savedGames.getObjects());
        return "saves";
    }

    @GetMapping("/load/{gameId}")
    public String loadGame(@PathVariable Long gameId, Model model) {
        GameDto game = gameService.loadGame(gameId);
        model.addAttribute("game", game);
        return "game";
    }
}
