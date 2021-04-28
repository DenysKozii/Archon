package com.company.archon.services.impl;

import com.company.archon.dto.QuestionDto;
import com.company.archon.entity.*;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.mapper.QuestionMapper;
import com.company.archon.pagination.PageDto;
import com.company.archon.pagination.PagesUtility;
import com.company.archon.repositories.*;
import com.company.archon.services.QuestionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final ParameterRepository parameterRepository;
    private final GamePatternRepository gamePatternRepository;
    private final AnswerRepository answerRepository;
    private final AnswerParameterRepository answerParameterRepository;
    private final QuestionParameterRepository questionParameterRepository;

    @Override
    public PageDto<QuestionDto> getQuestionsByGamePatternId(Long gamePatternId, int page, int pageSize) {
        Page<Question> result = questionRepository.findAllByGamePatternId(gamePatternId, PagesUtility.createPageableUnsorted(page, pageSize));
        return PageDto.of(result.getTotalElements(), page, mapToDto(result.getContent()));
    }

    @Override
    public PageDto<QuestionDto> getRelativeQuestionsByGamePatternId(Long gamePatternId, Long questionId, int page, int pageSize) {
        Page<Question> result = questionRepository.findAllByGamePatternId(gamePatternId, PagesUtility.createPageableUnsorted(page, pageSize));
        List<Question> questions = result.getContent().stream()
                .filter(o-> !o.getId().equals(questionId))
                .collect(Collectors.toList());
        return PageDto.of(result.getTotalElements(), page, mapToDto(questions));
    }

    @Override
    public PageDto<QuestionDto> addRelativeQuestion(Long questionId, Long relativeId, int page, int pageSize) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question with id " + questionId + " not found"));
        Question relativeQuestion = questionRepository.findById(relativeId)
                .orElseThrow(() -> new EntityNotFoundException("Question with id " + relativeId + " not found"));

        question.getQuestionConditions().add(relativeQuestion);
        questionRepository.save(question);

        Set<Question> relativeQuestions = new HashSet<>(question.getQuestionConditions());
        relativeQuestions.add(question);

        Page<Question> questions = questionRepository.findAllByGamePatternId(question.getGamePattern().getId(), PagesUtility.createPageableUnsorted(page, pageSize));
        List<Question> result = questions.getContent().stream()
                .filter(o -> !relativeQuestions.contains(o))
                .collect(Collectors.toList());

        return PageDto.of(result.size(), page, mapToDto(result));
    }

    private List<QuestionDto> mapToDto(List<Question> questions) {
        return questions.stream()
                .map(QuestionMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    private Question createNewQuestionParameters(GamePattern gamePattern) {
        Question question = new Question();
        question.setGamePattern(gamePattern);
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

    @Override
    public QuestionDto createNewQuestion(Long gamePatternId) {
        GamePattern gamePattern = gamePatternRepository.findById(gamePatternId)
                .orElseThrow(() -> new EntityNotFoundException("GamePattern with id " + gamePatternId + " not found"));
        Question question = createNewQuestionParameters(gamePattern);
        questionRepository.save(question);
        return QuestionMapper.INSTANCE.mapToDto(question);
    }

    @Override
    public PageDto<QuestionDto> getRelativeQuestions(Long questionId, int page, int pageSize) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question with id " + questionId + " not found"));
        PageDto<QuestionDto> pageDto = getQuestionsByGamePatternId(question.getGamePattern().getId(), page, pageSize);
        pageDto.getObjects().remove(question.getId().intValue());
        return pageDto;
    }

//    @Override
//    public QuestionDto createNewQuestion(Long gamePatternId) {
//        GamePattern gamePattern = gamePatternRepository.findById(gamePatternId)
//                .orElseThrow(() -> new EntityNotFoundException("GamePattern with id " + gamePatternId + " not found"));
//        Question question = new Question();
//        question.setGamePattern(gamePattern);
//        List<Parameter> parameters = parameterRepository.findAllByGamePattern(gamePattern);
//        for (Parameter parameter: parameters) {
//            QuestionParameter questionParameter = new QuestionParameter();
//            questionParameter.setTitle(parameter.getTitle());
//            questionParameter.setValueAppear(parameter.getLowestValue());
//            questionParameter.setValueDisappear(parameter.getHighestValue());
//
//            questionParameter.setQuestion(question);
//            questionParameterRepository.save(questionParameter);
//        }
//        questionRepository.save(question);
//        return QuestionMapper.INSTANCE.mapToDto(question);
//    }

    @Override
    public boolean deleteById(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question with id " + questionId + " not found"));
        List<Answer> answers = answerRepository.findAllByQuestionId(questionId);
        answers.stream().peek(
                answer -> answer.getParameters().forEach(answerParameterRepository::delete)
        ).forEach(answerRepository::delete);
        question.getQuestionParameters().forEach(questionParameterRepository::delete);
        question.setGamePattern(null);
        questionRepository.delete(question);
        return true;
    }

    @Override
    public boolean updateQuestion(Long gamePatternId, Long questionId, String title, String context, Integer weight, MultipartFile multipartFile) throws IOException {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question with id " + questionId + " not found"));
        question.setTitle(title);
        question.setContext(context);
        question.setWeight(weight);
//        String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
//        if (!fileName.isEmpty()){
//            question.setImage(String.format("/uploads/%s", fileName));
//            String uploadDir = request.getServletContext().getRealPath("/uploads");
//            Files.createDirectories(Paths.get(uploadDir));
//            multipartFile.transferTo(new File(String.format("%s/%s",uploadDir, fileName)));
//        }
        questionRepository.save(question);
        return true;
    }


}
