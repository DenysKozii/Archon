package com.company.archon.services.impl;

import com.company.archon.dto.GamePatternDto;
import com.company.archon.entity.*;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.mapper.GamePatternMapper;
import com.company.archon.pagination.PageDto;
import com.company.archon.pagination.PagesUtility;
import com.company.archon.repositories.ConditionParameterRepository;
import com.company.archon.repositories.GamePatternRepository;
import com.company.archon.repositories.UserRepository;
import com.company.archon.services.AuthorizationService;
import com.company.archon.services.GamePatternService;
import com.company.archon.services.GameService;
import com.company.archon.services.QuestionService;
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
    private final GameService gameService;
    private final QuestionService questionService;
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

    private void fillParameter(String title, GamePattern gamePattern){
        ConditionParameter parameter = new ConditionParameter();
        parameter.setTitle(title);
        parameter.setValue(0);
        parameter.setGamePattern(gamePattern);
        conditionParameterRepository.save(parameter);
    }

    private void fillConditionParameters(GamePattern gamePattern) {
        if (conditionParameterRepository.findAllByGamePatternId(gamePattern.getId()).size() < 9){
//            fillParameter("осколки душ", gamePattern);
//            fillParameter("прохождений чистилища", gamePattern);
//            fillParameter("расположенность к магии", gamePattern);
//            fillParameter("способности к эфиру", gamePattern);
//            fillParameter("созидание", gamePattern);
//            fillParameter("барьер", gamePattern);
//            fillParameter("контроль тела", gamePattern);
//            fillParameter("исцеление", gamePattern);
//            fillParameter("усиление", gamePattern);
            fillParameter("этаж", gamePattern);
        }
    }

    @Override
    public PageDto<GamePatternDto> getGamePatterns(int page, int pageSize) {
        String username = authorizationService.getProfileOfCurrent().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " doesn't exists!"));

        List<GamePattern> available = availableByUser(user);
        Page<GamePattern> result = gamePatternRepository.findAll(PagesUtility.createPageableUnsorted(page, pageSize));

        List<GamePatternDto> gamePatternDtos = mapGamePattern(result.getContent()).stream()
                .filter(o -> !o.getDeleted())
                .collect(Collectors.toList());
        List<String> availableTitles = mapGamePattern(available).stream()
                .map(GamePatternDto::getTitle)
                .collect(Collectors.toList());

        gamePatternDtos.stream()
                .filter(gp -> availableTitles.contains(gp.getTitle()))
                .forEach(o -> o.setAvailable(true));
        gamePatternDtos.sort(Comparator.comparingLong(GamePatternDto::getOrderId));
        return PageDto.of(result.getTotalElements(), page, gamePatternDtos);
    }

    private List<GamePattern> availableByUser(User user) {
        List<GamePattern> gamePatterns = gamePatternRepository.findAll();
        return gamePatterns.stream()
                .filter(o -> available(o, user))
                .collect(Collectors.toList());
    }

    private boolean available(GamePattern gamePattern, User user) {
        for (ConditionParameter conditionParameter : gamePattern.getConditionParameters()) {
            for (UserParameter userParameter : user.getUserParameters()) {
                if (conditionParameter.getTitle().equals(userParameter.getTitle())
                        && conditionParameter.getValue() > userParameter.getValue())
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
//        gameService.freeData();
        //        gamePattern.setDeleted(true);
//        gamePattern.setTitle("");
        gamePattern.getQuestions().forEach(o->questionService.deleteById(o.getId()));
//        gamePattern.getGames().forEach(o->gameService.deleteById(o.getId()));
        gameService.freeDataByGamePattern(gamePattern);
//        gamePattern.getParameters().forEach(o->parameterService.deleteById(o.getId()));
//        gamePattern.getConditionParameters().forEach(conditionParameterRepository::delete);
        gamePatternRepository.delete(gamePattern);
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
