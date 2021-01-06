package com.company.archon.services.impl;

import com.company.archon.dto.ParameterDto;
import com.company.archon.dto.QuestionDto;
import com.company.archon.dto.QuestionParameterDto;
import com.company.archon.entity.*;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.mapper.QuestionMapper;
import com.company.archon.repositories.*;
import com.company.archon.services.QuestionService;
import liquibase.pro.packaged.A;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final GamePatternRepository gamePatternRepository;
    private final AnswerRepository answerRepository;
    private final QuestionParameterRepository questionParameterRepository;
    private final ParameterRepository parameterRepository;

    public QuestionServiceImpl(QuestionRepository questionRepository, GamePatternRepository gamePatternRepository, AnswerRepository answerRepository, QuestionParameterRepository questionParameterRepository, ParameterRepository parameterRepository) {
        this.questionRepository = questionRepository;
        this.gamePatternRepository = gamePatternRepository;
        this.answerRepository = answerRepository;
        this.questionParameterRepository = questionParameterRepository;
        this.parameterRepository = parameterRepository;
    }

    @Override
    public List<QuestionDto> getQuestionsByGamePatternId(Long gamePatternId, Long questionId) {
        List<Question> questions = questionRepository.findAllByGamePatternId(gamePatternId);
        return questions.stream()
                .filter(o-> !o.getId().equals(questionId))
                .map(QuestionMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<QuestionDto> getQuestionsByGamePatternId(Long gamePatternId) {
        List<Question> questions = questionRepository.findAllByGamePatternId(gamePatternId);
        return questions.stream()
                .map(QuestionMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }




    @Override
    public QuestionDto createNewQuestion(Long gamePatternId) {
        GamePattern gamePattern = gamePatternRepository.findById(gamePatternId)
                .orElseThrow(() -> new EntityNotFoundException("GamePattern with id " + gamePatternId + " not found"));
        Question question = new Question();
        question.setGamePattern(gamePattern);
        List<Parameter> parameters = parameterRepository.findAllByGamePattern(gamePattern);
        for (Parameter parameter: parameters) {
            QuestionParameter questionParameter = new QuestionParameter();
            questionParameter.setTitle(parameter.getTitle());
            questionParameter.setValueAppear(parameter.getLowestValue());
            questionParameter.setValueDisappear(parameter.getHighestValue());

            questionParameter.setQuestion(question);
            questionParameterRepository.save(questionParameter);
        }
        questionRepository.save(question);
        return QuestionMapper.INSTANCE.mapToDto(question);
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

    @Override
    public List<QuestionDto> addRelativeQuestion(Long questionId, Long relativeId, Long gamePatternId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question with id " + questionId + " not found"));
        Question relativeQuestion = questionRepository.findById(relativeId)
                .orElseThrow(() -> new EntityNotFoundException("Question with id " + relativeId + " not found"));
        GamePattern gamePattern = gamePatternRepository.findById(gamePatternId)
                .orElseThrow(() -> new EntityNotFoundException("GamePattern with id " + gamePatternId + " not found"));

        question.getQuestionConditions().add(relativeQuestion);
        questionRepository.save(question);

        Set<Question> relativeQuestions = new HashSet<>(question.getQuestionConditions());
        relativeQuestions.add(question);
        return gamePattern.getQuestions().stream()
                .filter(o->!relativeQuestions.contains(o))
                .map(QuestionMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean deleteQuestion(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question with id " + questionId + " not found"));
        List<Answer> answers = answerRepository.findAllByQuestionId(questionId);
        answers.forEach(answerRepository::delete);
        questionRepository.delete(question);
        return true;
    }


}
