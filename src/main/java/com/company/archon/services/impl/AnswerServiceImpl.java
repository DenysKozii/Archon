package com.company.archon.services.impl;

import com.company.archon.dto.AnswerDto;
import com.company.archon.dto.GameDto;
import com.company.archon.dto.QuestionDto;
import com.company.archon.entity.*;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.mapper.AnswerMapper;
import com.company.archon.mapper.QuestionMapper;
import com.company.archon.pagination.PageDto;
import com.company.archon.pagination.PagesUtility;
import com.company.archon.repositories.*;
import com.company.archon.services.AnswerService;
import com.company.archon.services.AuthorizationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
    private final QuestionRepository questionRepository;
    private final QuestionParameterRepository questionParameterRepository;
    private final AnswerParameterRepository answerParameterRepository;
    private final UserRepository userRepository;
    private final UserParameterRepository userParameterRepository;
    private final AnswerUserParameterRepository answerUserParameterRepository;
    private final AuthorizationService authorizationService;

    @Override
    public List<AnswerDto> getAnswersByQuestionId(Long questionId) {
        List<Answer> answers = answerRepository.findAllByQuestionId(questionId);
        return answers.stream()
                .map(AnswerMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

//    @Override
//    public PageDto<AnswerDto> getAnswersByQuestionId(Long questionId, int page, int pageSize) {
//        Page<Answer> result = answerRepository.findAllByQuestionId(questionId, PagesUtility.createPageableUnsorted(page, pageSize));
//        return PageDto.of(result.getTotalElements(), page, mapToDto(result.getContent()));
//    }

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
        answer.setCounter(0);
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
        String username = authorizationService.getProfileOfCurrent().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " doesn't exists!"));
        List<UserParameter> userParameters = userParameterRepository.findAllByUser(user);
        for (UserParameter parameter: userParameters) {
            AnswerUserParameter answerUserParameter = new AnswerUserParameter();
            answerUserParameter.setTitle(parameter.getTitle());
            answerUserParameter.setValue(0);
            answerUserParameter.setAnswer(answer);
            answerUserParameterRepository.save(answerUserParameter);
            answer.getUserParameters().add(answerUserParameter);
        }

        answerRepository.save(answer);
        return AnswerMapper.INSTANCE.mapToDto(answer);
    }

    @Override
    public List<AnswerDto> getAll() {
        List<Answer> answers = answerRepository.findAll();
        return answers.stream()
                .map(AnswerMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionDto> getQuestionsByAnswers() {
        List<Answer> answers = answerRepository.findAll();
        return answers.stream()
                .map(Answer::getQuestion)
                .map(QuestionMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateCounter(Long answerId, Integer counter) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException("Answer with id " + answerId + " doesn't exists!"));
        answer.setCounter(counter);
        answerRepository.save(answer);
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
