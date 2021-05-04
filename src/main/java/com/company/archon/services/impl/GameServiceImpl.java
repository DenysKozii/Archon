package com.company.archon.services.impl;

import com.company.archon.dto.*;
import com.company.archon.entity.*;
import com.company.archon.enums.GameStatus;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.mapper.GameMapper;
import com.company.archon.mapper.QuestionMapper;
import com.company.archon.pagination.PageDto;
import com.company.archon.pagination.PagesUtility;
import com.company.archon.repositories.*;
import com.company.archon.services.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final GamePatternRepository gamePatternRepository;
    private final QuestionRepository questionRepository;
    private final ParameterRepository parameterRepository;
    private final GameParameterRepository gameParameterRepository;
    private final GameParameterService gameParameterService;
    private final QuestionParameterRepository questionParameterRepository;
    private final UserRepository userRepository;
    private final AnswerService answerService;
    private final AnswerRepository answerRepository;
    private final AnswerParameterRepository answerParameterRepository;
    private final AnswerUserParameterRepository answerUserParameterRepository;
    private final QuestionUserParameterRepository questionUserParameterRepository;
    private final UserParameterRepository userParameterRepository;
    private final AuthorizationService authorizationService;


    @Override
    public GameDto startNewGame(Long gamePatternId) {
        String username = authorizationService.getProfileOfCurrent().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " doesn't exists!"));

        GamePattern gamePattern = gamePatternRepository.findById(gamePatternId)
                .orElseThrow(() -> new EntityNotFoundException("GamePattern with id " + gamePatternId + " not found"));

        Game game = new Game();
        game.setGameStatus(GameStatus.RUNNING);
        game.setGamePattern(gamePattern);
        createParameters(game, gamePattern);
        game.setUser(user);
        gameRepository.save(game);
        game.setQuestionsPull(changeQuestions(game));
        gameRepository.save(game);

        return mapToDto(game);
    }

    private GameDto mapToDto(Game game) {
        GameDto gameDto = new GameDto();
        gameDto.setId(game.getId());
        gameDto.setGameStatus(game.getGameStatus());
        gameDto.setGamePatternId(game.getGamePattern().getId());
        gameDto.setGameStatus(game.getGameStatus());

        QuestionDto question = nextQuestion(game);
        gameDto.setQuestion(question);

        if (question != null) {
            List<AnswerDto> answers = answerService.getAnswersByQuestionId(question.getId());
            gameDto.setAnswers(answers);

            game.setGameStatus(question.getStatus());
            gameRepository.save(game);
        }

        List<GameParameterDto> parameters = gameParameterService.getByGameId(game.getId());
        gameDto.setParameters(parameters);

        return gameDto;
    }

    private void createParameters(Game game, GamePattern gamePattern) {
        List<Parameter> parameters = parameterRepository.findAllByGamePatternId(gamePattern.getId());
        for (Parameter parameter : parameters) {
            GameParameter gameParameter = new GameParameter();
            gameParameter.setParameter(parameter);
            gameParameter.setVisible(parameter.getVisible());
            gameParameter.setTitle(parameter.getTitle());
            gameParameter.setValue(parameter.getDefaultValue());
            gameParameter.setGame(game);
            gameParameterRepository.save(gameParameter);
            game.getParameters().add(gameParameter);
        }
    }

    private List<Question> changeQuestions(Game game) {
        String username = authorizationService.getProfileOfCurrent().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " doesn't exists!"));

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
                .filter(question -> questionUserParameterRepository
                        .findAllByQuestion(question).stream()
                        .noneMatch(parameter ->
                                parameter.getValueAppear() > userParameterRepository
                                        .findByTitleAndUser(parameter.getTitle(), user)
                                        .orElseThrow(() -> new EntityNotFoundException("UserParameter with title " + parameter.getTitle() + " not found"))
                                        .getValue()))
                .filter(o -> checkQuestions(game, o))
                .collect(Collectors.toList());
    }

    private boolean checkQuestions(Game game, Question question) {
        return game.getQuestionsPull().containsAll(question.getQuestionConditions());
    }

    @Override
    public boolean saveGame(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Game with id " + gameId + " not found"));
        game.setGameStatus(GameStatus.PAUSED);
        gameRepository.save(game);
        return true;
    }

    @Override
    public PageDto<GameDto> savedGames(int page, int pageSize) {
        String username = authorizationService.getProfileOfCurrent().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " doesn't exists!"));

        Page<Game> result = gameRepository.findByUser(user, PagesUtility.createPageableUnsorted(page, pageSize));
        return PageDto.of(result.getTotalElements(), page, mapToDto(result.getContent()));
    }

    private List<GameDto> mapToDto(List<Game> games) {
        List<GameDto> gameDtos = games.stream()
                .filter(o -> GameStatus.PAUSED.equals(o.getGameStatus()))
                .map(GameMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
        for (GameDto gameDto : gameDtos) {
            Game game = gameRepository.findById(gameDto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Game with id " + gameDto.getId() + " not found"));
            gameDto.setTitle(game.getGamePattern().getTitle());
        }
        return gameDtos;
    }

    @Override
    public GameDto loadGame(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Game with id " + gameId + " not found"));
        game.setGameStatus(GameStatus.RUNNING);
        gameRepository.save(game);
        return GameMapper.INSTANCE.mapToDto(game);
    }

    @Override
    public QuestionDto nextQuestion(Game game) {
        List<Question> questions = changeQuestions(game);
        game.setQuestionsPull(questions);
        gameRepository.save(game);
        if (questions.size() == 0)
            return null;
        Question question = randomQuestion(questions);
        return QuestionMapper.INSTANCE.mapToDto(question);
    }

    private Question randomQuestion(List<Question> questions) {
        long summary = questions.stream()
                .map(Question::getWeight)
                .reduce(0, Integer::sum);
        double random = Math.random() * summary;
        long counter = 0;
        for (Question question : questions) {
            counter += question.getWeight();
            if (counter >= random)
                return question;
        }
        return questions.get(0);
    }

    @Override
    public GameDto answerInfluence(Long answerId, Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Game with id " + gameId + " not found"));
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException("Answer with id " + answerId + " not found"));

        gameParameterRepository.findAllByGame(game)
                .forEach(o ->
                {
                    o.setValue(Integer.min(o.getParameter().getHighestValue(),
                            o.getValue() + answerParameterRepository
                                    .findByTitleAndAnswer(o.getParameter().getTitle(), answer)
                                    .orElseThrow(() -> new EntityNotFoundException("AnswerParameter with title " + o.getParameter().getTitle() + " not found"))
                                    .getValue()));
                    o.setValue(Integer.max(o.getParameter().getLowestValue(),
                            o.getValue()));
                });

        String username = authorizationService.getProfileOfCurrent().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " doesn't exists!"));
        user.getUserParameters()
                .forEach(o -> o.setValue(Integer.min(1,
                        o.getValue() + answerUserParameterRepository
                                .findByTitleAndAnswer(o.getTitle(), answer)
                                .orElseThrow(() -> new EntityNotFoundException("AnswerUserParameter with title " + o.getTitle() + " not found"))
                                .getValue())));

        gameRepository.save(game);
        game.setQuestionsPull(changeQuestions(game));
        gameRepository.save(game);

//        return gameOverConditionCheck(game);
        return mapToDto(game);

    }

    @Override
    public boolean deleteById(Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Game with id " + gameId + " not found"));
        game.getParameters().stream()
                .map(GameParameter::getId)
                .forEach(gameParameterService::deleteById);
        game.setGamePattern(null);
        game.setGameRequest(null);
        game.setUser(null);
        game.setQuestionsPull(new ArrayList<>());
        gameRepository.save(game);
        gameRepository.delete(game);
        return true;
    }


//    private boolean gameCompletedConditionCheck(Game game){
//        String username = authorizationService.getProfileOfCurrent().getUsername();
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " doesn't exists!"));
//
//        GamePattern gamePattern = game.getGamePattern();
//        for (ConditionParameter conditionParameter: gamePattern.getConditionParameters()) {
//            for (UserParameter userParameter:user.getUserParameters()) {
//                if (conditionParameter.getTitle().equals(userParameter.getTitle())
//                        && conditionParameter.getValueFinish() > 0
//                        && !(conditionParameter.getValueFinish().equals(userParameter.getValue())))
//                    return false;
//            }
//        }
//        return true;
//    }

}
