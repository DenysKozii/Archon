package com.company.archon.services.impl;

import com.company.archon.dto.QuestionParameterDto;
import com.company.archon.entity.Question;
import com.company.archon.entity.QuestionParameter;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.mapper.QuestionParameterMapper;
import com.company.archon.pagination.PageDto;
import com.company.archon.pagination.PagesUtility;
import com.company.archon.repositories.QuestionParameterRepository;
import com.company.archon.repositories.QuestionRepository;
import com.company.archon.services.QuestionParameterService;
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
public class QuestionParameterServiceImpl implements QuestionParameterService {
    private final QuestionParameterRepository questionParameterRepository;
    private final QuestionRepository questionRepository;

    @Override
    public PageDto<QuestionParameterDto> getParametersByQuestionId(Long questionId, int page, int pageSize) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new EntityNotFoundException("Question with id " + questionId + " not found"));

        Page<QuestionParameter> result = questionParameterRepository.findAllByQuestion(question, PagesUtility.createPageableUnsorted(page, pageSize));
        return PageDto.of(result.getTotalElements(), page, mapToDto(result.getContent()));
    }

    private List<QuestionParameterDto> mapToDto(List<QuestionParameter> questions) {
        return questions.stream()
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
