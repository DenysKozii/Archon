package com.company.archon.services.impl;

import com.company.archon.dto.GamePatternDto;
import com.company.archon.entity.*;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.mapper.GamePatternMapper;
import com.company.archon.pagination.PageDto;
import com.company.archon.pagination.PagesUtility;
import com.company.archon.repositories.GamePatternRepository;
import com.company.archon.repositories.QuestionRepository;
import com.company.archon.repositories.UserRepository;
import com.company.archon.services.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class GamePatternServiceImpl implements GamePatternService {
    private final GamePatternRepository gamePatternRepository;
    private final ParameterService parameterService;
    private final GameService gameService;
    private final QuestionService questionService;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final AuthorizationService authorizationService;


    @Override
    public GamePatternDto createGamePattern(String title) {
        String username = authorizationService.getProfileOfCurrent().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " doesn't exists!"));

        GamePattern gamePattern = new GamePattern();

        gamePattern.setOrderId((long) gamePatternRepository.findAll().size());
        gamePattern.setTitle(title);
        gamePattern.setDeleted(false);
        gamePattern.setUsersAmount(1);
        gamePattern.getUsers().add(user);
        gamePatternRepository.save(gamePattern);

        return GamePatternMapper.INSTANCE.mapToDto(gamePattern);
    }

    @Override
    public PageDto<GamePatternDto> getGamePatterns(int page, int pageSize) {
        String username = authorizationService.getProfileOfCurrent().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " doesn't exists!"));

        List<GamePattern> available = gamePatternRepository.findByUsers(user);
        Page<GamePattern> result = gamePatternRepository.findAll(PagesUtility.createPageableUnsorted(page, pageSize));

        List<GamePatternDto> gamePatternDtos = mapGamePattern(result.getContent()).stream()
                .filter(o->!o.getDeleted())
                .collect(Collectors.toList());
        List<String> availableTitles = mapGamePattern(available).stream()
                .map(GamePatternDto::getTitle)
                .collect(Collectors.toList());

        gamePatternDtos.stream()
                .filter(gp->availableTitles.contains(gp.getTitle()))
                .forEach(o->o.setAvailable(true));
        gamePatternDtos.sort(Comparator.comparingLong(GamePatternDto::getOrderId));
        return PageDto.of(result.getTotalElements(), page, gamePatternDtos);
    }

    private List<GamePatternDto> mapGamePattern(List<GamePattern> source) {
        return source.stream()
                .map(GamePatternMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteById(Long gamePatternId) {
        GamePattern gamePattern = gamePatternRepository.findById(gamePatternId)
                .orElseThrow(() -> new EntityNotFoundException("GamePattern with id " + gamePatternId + " not found"));
        gamePattern.setDeleted(true);
        gamePattern.setTitle("");
        gamePatternRepository.save(gamePattern);
        return true;
    }

    @Override
    public boolean updateAvailable(Long gamePatternId) {
        String username = authorizationService.getProfileOfCurrent().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " doesn't exists!"));
        GamePattern gamePattern = gamePatternRepository.findById(gamePatternId)
                .orElseThrow(() -> new EntityNotFoundException("GamePattern with id " + gamePatternId + " not found"));
        gamePattern.getUsers().add(user);
        gamePatternRepository.save(gamePattern);
        return true;
    }
}
