package com.company.archon.services;


import com.company.archon.dto.GameDto;
import com.company.archon.dto.QuestionDto;
import com.company.archon.entity.Game;
import com.company.archon.pagination.PageDto;

public interface GameService {

    QuestionDto nextQuestion(Game game);

    GameDto startNewGame(Long gamePatternId);

    GameDto answerInfluence(Long answerId, Long gameId);

    boolean deleteById(Long gameId);
}
