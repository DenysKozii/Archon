package com.company.archon.services;


import com.company.archon.dto.GamePatternDto;
import com.company.archon.pagination.PageDto;

public interface GamePatternService {
    GamePatternDto createGamePattern(String title);

    PageDto<GamePatternDto> getGamePatterns(int page, int pageSize);

    boolean deleteById(Long gamePatternId);

    boolean updateAvailable(Long gamePatternId);
}
