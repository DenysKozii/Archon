package com.company.archon.services.impl;

import com.company.archon.dto.GameDto;
import com.company.archon.dto.GamePatternDto;
import com.company.archon.dto.QuestionDto;
import com.company.archon.dto.UserDto;
import com.company.archon.entity.*;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.mapper.GameMapper;
import com.company.archon.mapper.QuestionMapper;
import com.company.archon.repositories.*;
import com.company.archon.services.GameService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GamePatternRepository gamePatternRepository;
    private final QuestionRepository questionRepository;
    private final ParameterRepository parameterRepository;
    private final GameParameterRepository gameParameterRepository;
    private final QuestionParameterRepository questionParameterRepository;
    private final UserRepository userRepository;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository, GamePatternRepository gamePatternRepository, QuestionRepository questionRepository, ParameterRepository parameterRepository, GameParameterRepository gameParameterRepository, QuestionParameterRepository questionParameterRepository, UserRepository userRepository) {
        this.gameRepository = gameRepository;
        this.gamePatternRepository = gamePatternRepository;
        this.questionRepository = questionRepository;
        this.parameterRepository = parameterRepository;
        this.gameParameterRepository = gameParameterRepository;
        this.questionParameterRepository = questionParameterRepository;
        this.userRepository = userRepository;
    }

    @Override
    public GameDto startNewGame(User user, Long gamePatternId) {
        GamePattern gamePattern = gamePatternRepository.findById(gamePatternId)
                .orElseThrow(() -> new EntityNotFoundException("GamePattern with id " + gamePatternId + " not found"));
        Game game = new Game();
        game.setGamePattern(gamePattern);
        gameRepository.save(game);
        List<Parameter> parameters = parameterRepository.findAllByGamePattern(gamePattern);
        for (Parameter parameter : parameters) {
            GameParameter gameParameter = new GameParameter();
            gameParameter.setParameter(parameter);
            gameParameter.setTitle(parameter.getTitle());
            gameParameter.setValue(parameter.getDefaultValue());
            gameParameter.setGame(game);
            gameParameterRepository.save(gameParameter);
        }
        game.setQuestionsPull(changeQuestions(game));
        game.getUsers().add(user);
        gameRepository.save(game);
        GameDto gameDto = GameMapper.INSTANCE.mapToDto(game);
//        user.getGames().add(game);
//        userRepository.save(user);
        return gameDto;
    }

    @Override
    public GameDto startNewGame(User user, Long gamePatternId, String friendEmail) {
        GameDto gameDto = startNewGame(user, gamePatternId);
        Game game = gameRepository.findById(gameDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Game with id " + gameDto.getId() + " not found"));
        User friend = userRepository.findByEmail(friendEmail)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + friendEmail + " not found"));
        game.getUsers().add(friend);
        gameRepository.save(game);
        return gameDto;
    }

    private List<Question> changeQuestions(Game game) {
        List<Question> questions = questionRepository.findAllByGamePatternId(game.getGamePattern().getId());
        return questions.stream()
                .filter(question -> questionParameterRepository
                        .findAllByQuestion(question).stream()
                        .noneMatch(parameter ->
                                parameter.getValueAppear() > gameParameterRepository
                                        .findAllByTitleAndGame(parameter.getTitle(), game)
                                        .orElseThrow(() -> new EntityNotFoundException("GameParameter with title " + parameter.getTitle() + " not found")).getValue()
                                        || parameter.getValueDisappear() < gameParameterRepository
                                        .findAllByTitleAndGame(parameter.getTitle(), game)
                                        .orElseThrow(() -> new EntityNotFoundException("GameParameter with title " + parameter.getTitle() + " not found")).getValue()))
                .filter(o -> checkQuestions(game, o))
                .collect(Collectors.toList());
    }

    private boolean checkQuestions(Game game, Question question) {
        return game.getQuestionsPull().containsAll(question.getQuestionConditions());
    }

    @Override
    public GameDto saveGame(Long gameId) {
        return null;
    }

    @Override
    public List<GameDto> savedGames(String username) {
        return null;
    }

    @Override
    public GameDto loadGame(Long gameId) {
        return null;
    }

    @Override
    public QuestionDto nextQuestion(GameDto gameDto) {
        Game game = gameRepository.findById(gameDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Game with id " + gameDto.getId() + " not found"));
        List<Question> questions = changeQuestions(game);
        game.setQuestionsPull(questions);
        gameRepository.save(game);
        // choose question
        Collections.shuffle(questions); //Todo
        if (questions.size() == 0)
            return null;
        Question question = questions.get(0);
        return QuestionMapper.INSTANCE.mapToDto(question);
    }

    @Override
    public GameDto getGame(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Game with id " + gameId + " not found"));
        return GameMapper.INSTANCE.mapToDto(game);
    }
}
