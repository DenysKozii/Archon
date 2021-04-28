package com.company.archon.services;


import com.company.archon.dto.GameDto;
import com.company.archon.dto.QuestionDto;
import com.company.archon.entity.Game;
import com.company.archon.pagination.PageDto;

public interface GameService {

    boolean saveGame(Long gameId);

    PageDto<GameDto> savedGames(int page, int pageSize);

    GameDto loadGame(Long gameId);

    QuestionDto nextQuestion(Game game);

    GameDto startNewGame(Long gamePatternId);

    GameDto answerInfluence(Long answerId, Long gameId);

    boolean deleteById(Long gameId);

}
