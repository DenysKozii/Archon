package com.company.archon.services.impl;

import com.company.archon.dto.QuestionParameterDto;
import com.company.archon.entity.Question;
import com.company.archon.entity.QuestionParameter;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.mapper.QuestionParameterMapper;
import com.company.archon.repositories.QuestionParameterRepository;
import com.company.archon.repositories.QuestionRepository;
import com.company.archon.services.QuestionParameterService;
import liquibase.pro.packaged.L;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class QuestionParameterServiceImpl implements QuestionParameterService {
    private final QuestionParameterRepository questionParameterRepository;
    private final QuestionRepository questionRepository;

    @Autowired
    public QuestionParameterServiceImpl(QuestionParameterRepository questionParameterRepository, QuestionRepository questionRepository) {
        this.questionParameterRepository = questionParameterRepository;
        this.questionRepository = questionRepository;
    }

    @Override
    public List<QuestionParameterDto> getParametersByQuestionId(Long questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question with id " + questionId + " not found"));
        return questionParameterRepository.findAllByQuestion(question).stream()
                .map(QuestionParameterMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean update(Long parameterId, Integer appear, Integer disappear) {
        QuestionParameter questionParameter = questionParameterRepository.findById(parameterId)
                .orElseThrow(() -> new EntityNotFoundException("QuestionParameter with id " + parameterId + " not found"));
        questionParameter.setValueAppear(appear);
        questionParameter.setValueDisappear(disappear);
        questionParameterRepository.save(questionParameter);
        return true;
    }
}
