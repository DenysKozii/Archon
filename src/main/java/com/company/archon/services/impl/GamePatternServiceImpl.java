package com.company.archon.services.impl;

import com.company.archon.dto.GamePatternDto;
import com.company.archon.entity.*;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.mapper.GamePatternMapper;
import com.company.archon.pagination.PageDto;
import com.company.archon.pagination.PagesUtility;
import com.company.archon.repositories.ConditionParameterRepository;
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
import java.util.function.Predicate;
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
    private final ConditionParameterRepository conditionParameterRepository;
    private final AuthorizationService authorizationService;


    @Override
    public GamePatternDto createGamePattern(String title) {
        GamePattern gamePattern = new GamePattern();

        gamePattern.setOrderId((long) gamePatternRepository.findAll().size());
        gamePattern.setTitle(title);
        gamePattern.setDeleted(false);
        gamePattern.setUsersAmount(1);
        gamePatternRepository.save(gamePattern);
        fillConditionParameters(gamePattern);
        return GamePatternMapper.INSTANCE.mapToDto(gamePattern);
    }

    private void fillConditionParameters(GamePattern gamePattern) {
        ConditionParameter parameter1 = new ConditionParameter();
        parameter1.setTitle("Manipulation");
        parameter1.setValueStart(0);
        parameter1.setValueFinish(0);
        parameter1.setGamePattern(gamePattern);
        conditionParameterRepository.save(parameter1);

        ConditionParameter parameter2 = new ConditionParameter();
        parameter2.setTitle("Intellect");
        parameter2.setValueStart(0);
        parameter2.setValueFinish(0);
        parameter2.setGamePattern(gamePattern);
        conditionParameterRepository.save(parameter2);

        ConditionParameter parameter3 = new ConditionParameter();
        parameter3.setTitle("Knowledge");
        parameter3.setValueStart(0);
        parameter3.setValueFinish(0);
        parameter3.setGamePattern(gamePattern);
        conditionParameterRepository.save(parameter3);
    }

    @Override
    public PageDto<GamePatternDto> getGamePatterns(int page, int pageSize) {
        String username = authorizationService.getProfileOfCurrent().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " doesn't exists!"));

        List<GamePattern> available = availableByUser(user);
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

    private List<GamePattern> availableByUser(User user){
        List<GamePattern> gamePatterns = gamePatternRepository.findAll();
        return gamePatterns.stream()
                .filter(o->available(o, user))
                .collect(Collectors.toList());
    }

    private boolean available(GamePattern gamePattern, User user){
        for (ConditionParameter conditionParameter : gamePattern.getConditionParameters()) {
            for (UserParameter userParameter: user.getUserParameters()) {
                if (conditionParameter.getTitle().equals(userParameter.getTitle())
                        && conditionParameter.getValueStart() > 0
                        && !(conditionParameter.getValueStart().equals(userParameter.getValue())))
                    return false;
            }
        }
        return true;
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
        gamePatternRepository.save(gamePattern);
        return true;
    }
}
