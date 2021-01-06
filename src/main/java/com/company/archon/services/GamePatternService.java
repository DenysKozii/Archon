package com.company.archon.services;

import com.company.archon.dto.GamePatternDto;
import com.company.archon.entity.User;

import java.util.List;

public interface GamePatternService {
    GamePatternDto createGamePattern(String title, Integer usersAmount, User user);

    List<GamePatternDto> getGamePatternsByUser(User user);

    boolean deleteGamePattern(Long gamePatternId);

    List<GamePatternDto> getGamePatternsByUser(User user, String userEmail);
}
