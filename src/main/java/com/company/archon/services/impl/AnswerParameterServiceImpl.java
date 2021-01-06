package com.company.archon.services.impl;

import com.company.archon.dto.AnswerParameterDto;
import com.company.archon.entity.Answer;
import com.company.archon.entity.AnswerParameter;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.mapper.AnswerParameterMapper;
import com.company.archon.repositories.AnswerParameterRepository;
import com.company.archon.repositories.AnswerRepository;
import com.company.archon.services.AnswerParameterService;
import com.company.archon.services.GameParameterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class AnswerParameterServiceImpl implements AnswerParameterService {

    private final AnswerParameterRepository answerParameterRepository;
    private final AnswerRepository answerRepository;

    @Autowired
    public AnswerParameterServiceImpl(AnswerParameterRepository answerParameterRepository, AnswerRepository answerRepository) {
        this.answerParameterRepository = answerParameterRepository;
        this.answerRepository = answerRepository;
    }

    @Override
    public boolean update(Long parameterId, Integer value) {
        AnswerParameter answerParameter = answerParameterRepository.findById(parameterId)
                .orElseThrow(()->new EntityNotFoundException("AnswerParameter with id " + parameterId + " not found"));
        answerParameter.setValue(value);
        answerParameterRepository.save(answerParameter);
        return true;
    }

    @Override
    public List<AnswerParameterDto> getParametersByAnswerId(Long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(()->new EntityNotFoundException("Answer with id " + answerId + " not found"));
        return answerParameterRepository.findAllByAnswer(answer).stream()
                .map(AnswerParameterMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }
}
