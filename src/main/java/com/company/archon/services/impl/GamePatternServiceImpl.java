package com.company.archon.services.impl;

import com.company.archon.dto.GamePatternDto;
import com.company.archon.entity.GamePattern;
import com.company.archon.entity.User;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.mapper.GamePatternMapper;
import com.company.archon.repositories.GamePatternRepository;
import com.company.archon.repositories.QuestionRepository;
import com.company.archon.repositories.UserRepository;
import com.company.archon.services.GamePatternService;
import com.company.archon.services.QuestionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class GamePatternServiceImpl implements GamePatternService {
    private final GamePatternRepository gamePatternRepository;
    private final UserRepository userRepository;
    private final QuestionService questionService;
    private final QuestionRepository questionRepository;

    @Autowired
    public GamePatternServiceImpl(GamePatternRepository gamePatternRepository, UserRepository userRepository, QuestionService questionService, QuestionRepository questionRepository) {
        this.gamePatternRepository = gamePatternRepository;
        this.userRepository = userRepository;
        this.questionService = questionService;
        this.questionRepository = questionRepository;
    }

    @Override
    public GamePatternDto createGamePattern(String title, Integer usersAmount, User user) {
        if(gamePatternRepository.findByTitle(title).isPresent())
            return GamePatternMapper.INSTANCE.mapToDto(gamePatternRepository.findByTitle(title).get());
        GamePattern gamePattern = new GamePattern();
        gamePattern.setTitle(title);
        gamePattern.setUsersAmount(Integer.min(usersAmount,2));
        gamePattern.getUsers().add(user);
        gamePatternRepository.save(gamePattern);
//        user.getGamePatterns().add(gamePattern);
//        userRepository.save(user);
        return GamePatternMapper.INSTANCE.mapToDto(gamePattern);
    }

    @Override
    public List<GamePatternDto> getGamePatternsByUser(User user) {
        List<GamePattern> gamePatterns = gamePatternRepository.findByUsers(user);
        return gamePatterns.stream()
                .filter(o->o.getUsersAmount()==1)
                .map(GamePatternMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteGamePattern(Long gamePatternId) {
        GamePattern gamePattern = gamePatternRepository.findById(gamePatternId)
                .orElseThrow(()->new EntityNotFoundException("GamePattern with id " + gamePatternId + " not found"));
        questionRepository.findAllByGamePatternId(gamePatternId).forEach(o->questionService.deleteQuestion(o.getId()));
        userRepository.findAllByGamePatterns(gamePattern)
                .forEach(o->o.getGamePatterns().remove(gamePattern));
        gamePattern.setUsers(new HashSet<>());
        gamePatternRepository.delete(gamePattern);
        return true;
    }

    @Override
    public List<GamePatternDto> getGamePatternsByUser(User user, String userEmail) {
        List<GamePattern> gamePatterns = gamePatternRepository.findByUsers(user);
        return gamePatterns.stream()
                .filter(o->o.getUsersAmount()==2)
                .map(GamePatternMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }
}
