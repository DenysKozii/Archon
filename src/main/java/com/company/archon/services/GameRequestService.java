package com.company.archon.services;

import com.company.archon.dto.GameDto;
import com.company.archon.dto.GameRequestDto;
import com.company.archon.pagination.PageDto;

public interface GameRequestService {

    PageDto<GameRequestDto> findAllByUsername(int page, int pageSize);

    GameRequestDto createGameRequest(String friendEmail, Long gamePatternId);

    boolean acceptGameRequest(Long gameRequestId);

    boolean rejectGameRequest(Long gameRequestId);

    GameDto startGameRequest(Long gameRequestId);
}
