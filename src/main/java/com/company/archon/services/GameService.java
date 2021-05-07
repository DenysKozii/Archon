package com.company.archon.services;


import com.company.archon.dto.GameDto;
import com.company.archon.dto.QuestionDto;
import com.company.archon.entity.Game;
import com.company.archon.entity.GamePattern;
import com.company.archon.pagination.PageDto;

public interface GameService {

    GameDto startNewGame(Long gamePatternId);

    GameDto answerInfluence(Long answerId, Long gameId);

    boolean deleteById(Long gameId);

    void freeData();

    void freeDataByGamePattern(GamePattern gamePattern);
}
