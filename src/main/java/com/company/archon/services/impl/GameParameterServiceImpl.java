package com.company.archon.services.impl;

import com.company.archon.dto.GameParameterDto;
import com.company.archon.entity.Game;
import com.company.archon.entity.GameParameter;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.mapper.GameParameterMapper;
import com.company.archon.repositories.GameParameterRepository;
import com.company.archon.repositories.GameRepository;
import com.company.archon.services.GameParameterService;
import com.company.archon.services.GamePatternService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class GameParameterServiceImpl implements GameParameterService {
    private final GameParameterRepository gameParameterRepository;
    private final GameRepository gameRepository;

    @Autowired
    public GameParameterServiceImpl(GameParameterRepository gameParameterRepository, GameRepository gameRepository) {
        this.gameParameterRepository = gameParameterRepository;
        this.gameRepository = gameRepository;
    }

    @Override
    public List<GameParameterDto> getByGameId(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Game with id " + id + " not found"));
        return gameParameterRepository.findAllByGame(game).stream()
                .map(GameParameterMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }
}
