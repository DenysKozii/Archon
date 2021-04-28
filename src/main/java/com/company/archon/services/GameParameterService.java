package com.company.archon.services;

import com.company.archon.dto.GameParameterDto;

import java.util.List;

public interface GameParameterService {

    List<GameParameterDto> getByGameId(Long id);

    boolean deleteById(Long id);
}
