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
        ConditionParameter parameter0 = new ConditionParameter();
        parameter0.setTitle("Souls");
        parameter0.setValue(0);
        parameter0.setGamePattern(gamePattern);
        conditionParameterRepository.save(parameter0);
//        ConditionParameter parameter0 = new ConditionParameter();
//        parameter0.setTitle("Осколки душ");
//        parameter0.setValue(0);
//        parameter0.setGamePattern(gamePattern);
//        conditionParameterRepository.save(parameter0);

//        ConditionParameter parameter1 = new ConditionParameter();
//        parameter1.setTitle("Талант к магии");
//        parameter1.setValue(0);
//        parameter1.setGamePattern(gamePattern);
//        conditionParameterRepository.save(parameter1);
//
//        ConditionParameter parameter2 = new ConditionParameter();
//        parameter2.setTitle("Смышленность");
//        parameter2.setValue(0);
//        parameter2.setGamePattern(gamePattern);
//        conditionParameterRepository.save(parameter2);
//
//        ConditionParameter parameter3 = new ConditionParameter();
//        parameter3.setTitle("Призрение к миру");
//        parameter3.setValue(0);
//        parameter3.setGamePattern(gamePattern);
//        conditionParameterRepository.save(parameter3);
//
//        ConditionParameter parameter4 = new ConditionParameter();
//        parameter4.setTitle("Талант к эфиру");
//        parameter4.setValue(0);
//        parameter4.setGamePattern(gamePattern);
//        conditionParameterRepository.save(parameter4);
//
//        ConditionParameter parameter5 = new ConditionParameter();
//        parameter5.setTitle("Талант к духу");
//        parameter5.setValue(0);
//        parameter5.setGamePattern(gamePattern);
//        conditionParameterRepository.save(parameter5);
//
//        ConditionParameter parameter6 = new ConditionParameter();
//        parameter6.setTitle("Харизма");
//        parameter6.setValue(0);
//        parameter6.setGamePattern(gamePattern);
//        conditionParameterRepository.save(parameter6);
//
//        ConditionParameter parameter7 = new ConditionParameter();
//        parameter7.setTitle("Интеллект");
//        parameter7.setValue(0);
//        parameter7.setGamePattern(gamePattern);
//        conditionParameterRepository.save(parameter7);
//
//        ConditionParameter parameter8 = new ConditionParameter();
//        parameter8.setTitle("Жестокость");
//        parameter8.setValue(0);
//        parameter8.setGamePattern(gamePattern);
//        conditionParameterRepository.save(parameter8);
//
//        ConditionParameter parameter9 = new ConditionParameter();
//        parameter9.setTitle("Манипуляции");
//        parameter9.setValue(0);
//        parameter9.setGamePattern(gamePattern);
//        conditionParameterRepository.save(parameter9);
//
//        ConditionParameter parameter10 = new ConditionParameter();
//        parameter10.setTitle("Единость с миром");
//        parameter10.setValue(0);
//        parameter10.setGamePattern(gamePattern);
//        conditionParameterRepository.save(parameter10);
//
//        ConditionParameter parameter11 = new ConditionParameter();
//        parameter11.setTitle("Правление");
//        parameter11.setValue(0);
//        parameter11.setGamePattern(gamePattern);
//        conditionParameterRepository.save(parameter11);
//
//        ConditionParameter parameter12 = new ConditionParameter();
//        parameter12.setTitle("Гениальность");
//        parameter12.setValue(0);
//        parameter12.setGamePattern(gamePattern);
//        conditionParameterRepository.save(parameter12);
//
//        ConditionParameter parameter13 = new ConditionParameter();
//        parameter13.setTitle("Кровожадность");
//        parameter13.setValue(0);
//        parameter13.setGamePattern(gamePattern);
//        conditionParameterRepository.save(parameter13);
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
//        gamePattern.getQuestions().forEach(o->questionService.deleteById(o.getId()));
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
