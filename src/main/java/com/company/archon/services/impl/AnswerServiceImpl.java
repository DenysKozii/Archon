package com.company.archon.services.impl;

import com.company.archon.dto.AnswerDto;
import com.company.archon.dto.GameDto;
import com.company.archon.entity.*;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.mapper.AnswerMapper;
import com.company.archon.pagination.PageDto;
import com.company.archon.pagination.PagesUtility;
import com.company.archon.repositories.*;
import com.company.archon.services.AnswerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;
    private final GameRepository gameRepository;
    private final QuestionRepository questionRepository;
    private final GameParameterRepository gameParameterRepository;
    private final QuestionParameterRepository questionParameterRepository;
    private final AnswerParameterRepository answerParameterRepository;

    @Override
    public List<AnswerDto> getAnswersByQuestionId(Long questionId) {
        List<Answer> answers = answerRepository.findAllByQuestionId(questionId);
        return answers.stream()
                .map(AnswerMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public PageDto<AnswerDto> getAnswersByQuestionId(Long questionId, int page, int pageSize) {
        Page<Answer> result = answerRepository.findAllByQuestionId(questionId, PagesUtility.createPageableUnsorted(page, pageSize));
        return PageDto.of(result.getTotalElements(), page, mapToDto(result.getContent()));
    }

    private List<AnswerDto> mapToDto(List<Answer> answers) {
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
    public GameDto answerInfluence(Long answerId, GameDto gameDto, GameDto gameDtoSecond) {
//        Answer answer = answerRepository.findById(answerId)
//                .orElseThrow(() -> new EntityNotFoundException("Answer with id " + answerId + " not found"));
//        Game game = gameRepository.findById(gameDto.getId())
//                .orElseThrow(() -> new EntityNotFoundException("Game with id " + gameDto.getId() + " not found"));
//        List<GameParameter> gameParameters = gameParameterRepository.findAllByGame(game);
//        gameParameters
//                .forEach(o->o.setValue(Integer.min(o.getParameter().getHighestValue(),
//                        o.getValue()+answerParameterRepository
//                                .findByTitleAndAnswer(o.getParameter().getTitle(), answer)
//                                .orElseThrow(() -> new EntityNotFoundException("AnswerParameter with title " + o.getParameter().getTitle() + " not found"))
//                                .getValue())));
//        gameRepository.save(game);
//        game = gameRepository.findById(gameDtoSecond.getId())
//                .orElseThrow(() -> new EntityNotFoundException("Game with id " + gameDtoSecond.getId() + " not found"));
//
//        gameParameters = gameParameterRepository.findAllByGame(game);
//        gameParameters
//                .forEach(o->o.setValue(Integer.min(o.getParameter().getHighestValue(),
//                        o.getValue()+answerParameterRepository
//                                .findByTitleAndAnswer(o.getParameter().getTitle(), answer)
//                                .orElseThrow(() -> new EntityNotFoundException("AnswerParameter with title " + o.getParameter().getTitle() + " not found"))
//                                .getValue())));
//        gameRepository.save(game);
//
//        return gameOverConditionCheck(game);
        return null;
    }


    @Override
    public boolean deleteById(Long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException("Answer with id " + answerId + " not found"));
        List<AnswerParameter> parameters = answer.getParameters();
        parameters.forEach(answerParameterRepository::delete);
        answerRepository.delete(answer);
        return true;
    }

}
