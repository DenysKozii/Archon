package com.company.archon.services.impl;

import com.company.archon.dto.AnswerDto;
import com.company.archon.dto.GameDto;
import com.company.archon.entity.*;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.mapper.AnswerMapper;
import com.company.archon.repositories.*;
import com.company.archon.services.AnswerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final GameRepository gameRepository;
    private final QuestionRepository questionRepository;
    private final GameParameterRepository gameParameterRepository;
    private final QuestionParameterRepository questionParameterRepository;
    private final AnswerParameterRepository answerParameterRepository;

    @Autowired
    public AnswerServiceImpl(AnswerRepository answerRepository, GameRepository gameRepository, QuestionRepository questionRepository, GameParameterRepository gameParameterRepository, QuestionParameterRepository questionParameterRepository, AnswerParameterRepository answerParameterRepository) {
        this.answerRepository = answerRepository;
        this.gameRepository = gameRepository;
        this.questionRepository = questionRepository;
        this.gameParameterRepository = gameParameterRepository;
        this.questionParameterRepository = questionParameterRepository;
        this.answerParameterRepository = answerParameterRepository;
    }

    @Override
    public boolean answerInfluence(Long answerId, GameDto gameDto) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException("Answer with id " + answerId + " not found"));
        Game game = gameRepository.findById(gameDto.getId())
                .orElseThrow(() -> new EntityNotFoundException("Game with id " + gameDto.getId() + " not found"));
        List<GameParameter> gameParameters = gameParameterRepository.findAllByGame(game);
        gameParameters
                .forEach(o->o.setValue(Integer.min(o.getParameter().getHighestValue(),
                        o.getValue()+answerParameterRepository
                        .findByTitleAndAnswer(o.getParameter().getTitle(), answer)
                        .orElseThrow(() -> new EntityNotFoundException("AnswerParameter with title " + o.getParameter().getTitle() + " not found"))
                        .getValue())));
        gameRepository.save(game);
        return gameOverCondition(game);
    }

    @Override
    public List<AnswerDto> getAnswersByQuestionId(Long questionId) {
        List<Answer> answers = answerRepository.findAllByQuestionId(questionId);
        return answers.stream()
                .map(AnswerMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public AnswerDto createNewAnswer(Long questionId, String context) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question with id " + questionId + " not found"));
        Answer answer = new Answer();
        answer.setQuestion(question);
        answer.setContext(context);
        List<QuestionParameter> parameters = questionParameterRepository.findAllByQuestion(question);
        for (QuestionParameter parameter: parameters) {
            AnswerParameter answerParameter = new AnswerParameter();
            answerParameter.setTitle(parameter.getTitle());
            answerParameter.setValue(0);
            answerParameter.setAnswer(answer);
            answerParameterRepository.save(answerParameter);
            answer.getParameters().add(answerParameter);
        }
        answerRepository.save(answer);
        return AnswerMapper.INSTANCE.mapToDto(answer);
    }


    @Override
    public boolean deleteAnswerByQuestionId(Long questionId, Long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException("Answer with id " + answerId + " not found"));
        answerRepository.delete(answer);
        return true;
    }


    private boolean gameOverCondition(Game game){
        return gameParameterRepository.findAllByGame(game).stream()
                .anyMatch(o -> o.getValue() <= o.getParameter().getLowestValue());
    }

}
