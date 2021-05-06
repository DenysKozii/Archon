package com.company.archon.services.impl;

import com.company.archon.dto.QuestionDto;
import com.company.archon.entity.*;
import com.company.archon.enums.GameStatus;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.mapper.QuestionMapper;
import com.company.archon.pagination.PageDto;
import com.company.archon.pagination.PagesUtility;
import com.company.archon.repositories.*;
import com.company.archon.services.AuthorizationService;
import com.company.archon.services.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final QuestionUserParameterRepository questionUserParameterRepository;
    private final GamePatternRepository gamePatternRepository;
    private final AnswerRepository answerRepository;
    private final AnswerParameterRepository answerParameterRepository;
    private final QuestionParameterRepository questionParameterRepository;
    private final AuthorizationService authorizationService;
    private final UserRepository userRepository;

    @Override
    public PageDto<QuestionDto> getQuestionsByGamePatternId(Long gamePatternId, int page, int pageSize) {
        Page<Question> result = questionRepository.findAllByGamePatternId(gamePatternId, PagesUtility.createPageableUnsorted(page, pageSize));
        return PageDto.of(result.getTotalElements(), page, mapToDto(result.getContent()));
    }

    @Override
    public PageDto<QuestionDto> getRelativeQuestionsByGamePatternId(Long gamePatternId, Long questionId, int page, int pageSize) {
        Page<Question> result = questionRepository.findAllByGamePatternId(gamePatternId, PagesUtility.createPageableUnsorted(page, pageSize));
        List<Question> questions = result.getContent().stream()
                .filter(o -> !o.getId().equals(questionId))
                .collect(Collectors.toList());
        return PageDto.of(result.getTotalElements(), page, mapToDto(questions));
    }

    @Override
    public PageDto<QuestionDto> addRelativeQuestion(Long relativeId, Long questionId, int page, int pageSize) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question with id " + questionId + " not found"));
        Question relativeQuestion = questionRepository.findById(relativeId)
                .orElseThrow(() -> new EntityNotFoundException("Question with id " + relativeId + " not found"));

        question.setRelativeQuestion(relativeQuestion);
        questionRepository.save(question);

        Page<Question> questions = questionRepository.findAllByGamePatternId(question.getGamePattern().getId(), PagesUtility.createPageableUnsorted(page, pageSize));
        List<Question> result = questions.getContent().stream()
                .filter(o -> !relativeQuestion.equals(o.getRelativeQuestion()))
                .filter(o -> !relativeQuestion.equals(o))
                .collect(Collectors.toList());

        return PageDto.of(result.size(), page, mapToDto(result));
    }

    private List<QuestionDto> mapToDto(List<Question> questions) {
        return questions.stream()
                .map(QuestionMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public QuestionDto createNewQuestion(Long gamePatternId) {
        GamePattern gamePattern = gamePatternRepository.findById(gamePatternId)
                .orElseThrow(() -> new EntityNotFoundException("GamePattern with id " + gamePatternId + " not found"));
        Question question = createNewQuestionParameters(gamePattern);
        questionRepository.save(question);
        createNewQuestionUserParameters(question);
        questionRepository.save(question);
        return QuestionMapper.INSTANCE.mapToDto(question);
    }

    private Question createNewQuestionParameters(GamePattern gamePattern) {
        Question question = new Question();
        question.setGamePattern(gamePattern);
        question.setTitle("title");
        question.setContext("context");
        question.setWeight(1);
        for (Parameter parameter : gamePattern.getParameters()) {
            QuestionParameter questionParameter = new QuestionParameter();
            questionParameter.setTitle(parameter.getTitle());
            questionParameter.setValueAppear(parameter.getLowestValue());
            questionParameter.setValueDisappear(parameter.getHighestValue());
            questionParameter.setQuestion(question);
            questionParameterRepository.save(questionParameter);
            question.getQuestionParameters().add(questionParameter);
        }
        return question;
    }

    private void createNewQuestionUserParameters(Question question) {
        String username = authorizationService.getProfileOfCurrent().getUsername();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " doesn't exists!"));
        for (UserParameter parameter : user.getUserParameters()) {
            QuestionUserParameter questionParameter = new QuestionUserParameter();
            questionParameter.setTitle(parameter.getTitle());
            questionParameter.setValueAppear(0);
            questionParameter.setQuestion(question);
            questionUserParameterRepository.save(questionParameter);
            question.getQuestionUserParameters().add(questionParameter);
        }
    }

    @Override
    public PageDto<QuestionDto> getRelativeQuestions(Long questionId, int page, int pageSize) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question with id " + questionId + " not found"));
        PageDto<QuestionDto> pageDto = getQuestionsByGamePatternId(question.getGamePattern().getId(), page, pageSize);
        pageDto.getObjects().remove(question.getId().intValue());
        return pageDto;
    }

    @Override
    public boolean deleteById(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question with id " + questionId + " not found"));
        List<Answer> answers = answerRepository.findAllByQuestionId(questionId);
        question.setGamePattern(null);
        question.setRelativeQuestion(null);
        questionRepository.save(question);
        List<Question> relativeQuestions = questionRepository.findAllByRelativeQuestionId(questionId);
        for (Question relativeQuestion: relativeQuestions) {
            relativeQuestion.setRelativeQuestion(null);
            questionRepository.save(relativeQuestion);
        }
        answers.stream().peek(
                answer -> answer.getParameters().forEach(answerParameterRepository::delete)
        ).forEach(answerRepository::delete);
        question.getQuestionParameters().forEach(questionParameterRepository::delete);
        questionRepository.delete(question);
        return true;
    }

    @Override
    public QuestionDto getById(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question with id " + questionId + " not found"));
        return QuestionMapper.INSTANCE.mapToDto(question);
    }

    @Override
    public QuestionDto updateQuestion(Long gamePatternId, Long questionId, String title, String context, Integer weight, GameStatus status, MultipartFile multipartFile) throws IOException {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question with id " + questionId + " not found"));
        if (!title.isEmpty())
            question.setTitle(title);
        if (!context.isEmpty())
            question.setContext(context);
        if (Objects.nonNull(weight))
            question.setWeight(weight);
        question.setStatus(status);
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
            if (!fileName.isEmpty()) {
                question.setImage(Base64.getEncoder().encodeToString(multipartFile.getBytes()));
            }
        }
        questionRepository.save(question);
        return QuestionMapper.INSTANCE.mapToDto(question);
    }


}
