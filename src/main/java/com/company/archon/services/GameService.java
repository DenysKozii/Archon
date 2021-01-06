package com.company.archon.services;

import com.company.archon.dto.*;
import com.company.archon.entity.User;

import java.util.List;
import java.util.function.Predicate;

public interface GameService {

    GameDto saveGame(Long gameId);

    List<GameDto> savedGames(String username);

    GameDto loadGame(Long gameId);

    QuestionDto nextQuestion(GameDto game);

    GameDto getGame(Long gameId);

    GameDto startNewGame(User user, Long gamePatternId);

    GameDto startNewGame(User user, Long gamePatternId, String friendEmail);
}
