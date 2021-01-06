package com.company.archon.controllers;

import com.company.archon.dto.*;
import com.company.archon.entity.Answer;
import com.company.archon.entity.Game;
import com.company.archon.entity.GamePattern;
import com.company.archon.entity.User;
import com.company.archon.mapper.UserMapper;
import com.company.archon.services.*;
import com.company.archon.services.impl.GameServiceImpl;
import liquibase.pro.packaged.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/game")
public class GameController {
    private final AnswerService answerService;
    private final GameService gameService;
    private final GameParameterService gameParameterService;

    @Autowired
    public GameController(AnswerService answerService, GameService gameService, GameParameterService gameParameterService) {
        this.answerService = answerService;
        this.gameService = gameService;
        this.gameParameterService = gameParameterService;
    }

    @GetMapping("/new/{gamePatternId}")
    public String newGame(@PathVariable Long gamePatternId,
                          @AuthenticationPrincipal User user, Model model) {
        GameDto game = gameService.startNewGame(user, gamePatternId);
        addCommonAttributes(game, user, model);
        return "game";
    }

    @GetMapping("/new/{gamePatternId}/{friendEmail}")
    public String newGame(@PathVariable Long gamePatternId,
                          @PathVariable String friendEmail,
                          @AuthenticationPrincipal User user, Model model) {
        GameDto game = gameService.startNewGame(user, gamePatternId, friendEmail);
        addCommonAttributes(game, user, model);
        return "game";
    }


    @PostMapping("/answer/{gameId}/{answerId}")
    public String checkAnswer(@PathVariable Long gameId,
                              @PathVariable Long answerId,
                              @AuthenticationPrincipal User user,
                              Model model) {
        GameDto game = gameService.getGame(gameId);
        if(answerService.answerInfluence(answerId, game)) {
            model.addAttribute("username", user.getUsername())
                    .addAttribute("game", game);
            return "gameOver";
        }
        addCommonAttributes(game, user, model);
        return "game";
    }

    @PostMapping("/save/{gameId}")
    public String saveGame(@PathVariable Long gameId, @AuthenticationPrincipal User user, Model model) {
        GameDto game = gameService.saveGame(gameId);
        QuestionDto question = gameService.nextQuestion(game);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("question", question);
        return "game";
    }

    @GetMapping("/saved")
    public String savedGames(@AuthenticationPrincipal User user, Model model) {
        List<GameDto> savedGames = gameService.savedGames(user.getUsername());
        model.addAttribute("username", user.getUsername());
        model.addAttribute("savedGames", savedGames);
        return "saves";
    }

    @GetMapping("/load/{gameId}")
    public String loadGame(@PathVariable Long gameId, @AuthenticationPrincipal User user, Model model) {
        GameDto game = gameService.loadGame(gameId);
        QuestionDto question = gameService.nextQuestion(game);
        model.addAttribute("username", user.getUsername());
        model.addAttribute("question", question);
        return "game";
    }

    private void addCommonAttributes(GameDto game, User user, Model model) {
        QuestionDto question = gameService.nextQuestion(game);
        List<AnswerDto> answers = answerService.getAnswersByQuestionId(question.getId());
        List<GameParameterDto> parameters = gameParameterService.getByGameId(game.getId());
        model.addAttribute("username", user.getUsername())
                .addAttribute("question", question)
                .addAttribute("answers", answers)
                .addAttribute("game", game)
                .addAttribute("parameters", parameters);
    }
}
