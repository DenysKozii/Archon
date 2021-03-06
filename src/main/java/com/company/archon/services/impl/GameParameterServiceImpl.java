package com.company.archon.services.impl;

import com.company.archon.dto.GameParameterDto;
import com.company.archon.entity.Game;
import com.company.archon.entity.GameParameter;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.mapper.GameParameterMapper;
import com.company.archon.repositories.GameParameterRepository;
import com.company.archon.repositories.GameRepository;
import com.company.archon.services.GameParameterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class GameParameterServiceImpl implements GameParameterService {
    private final GameParameterRepository gameParameterRepository;
    private final GameRepository gameRepository;

    @Override
    public List<GameParameterDto> getByGameId(Long id) {
        Game game = gameRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("Game with id " + id + " not found"));
        return gameParameterRepository.findAllByGame(game).stream()
                .map(GameParameterMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(Long id) {
        GameParameter gameParameter = gameParameterRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException("GameParameter with id " + id + " not found"));
        gameParameter.setParameter(null);
        gameParameterRepository.save(gameParameter);
        gameParameterRepository.delete(gameParameter);
        return true;
    }
}
