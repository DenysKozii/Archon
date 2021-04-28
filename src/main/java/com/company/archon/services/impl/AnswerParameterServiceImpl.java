package com.company.archon.services.impl;

import com.company.archon.dto.AnswerParameterDto;
import com.company.archon.entity.Answer;
import com.company.archon.entity.AnswerParameter;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.mapper.AnswerParameterMapper;
import com.company.archon.pagination.PageDto;
import com.company.archon.pagination.PagesUtility;
import com.company.archon.repositories.AnswerParameterRepository;
import com.company.archon.repositories.AnswerRepository;
import com.company.archon.services.AnswerParameterService;
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
public class AnswerParameterServiceImpl implements AnswerParameterService {

    private final AnswerParameterRepository answerParameterRepository;
    private final AnswerRepository answerRepository;

    @Override
    public boolean update(Long parameterId, Integer value) {
        AnswerParameter answerParameter = answerParameterRepository.findById(parameterId)
                .orElseThrow(()->new EntityNotFoundException("AnswerParameter with id " + parameterId + " not found"));
        answerParameter.setValue(value);
        answerParameterRepository.save(answerParameter);
        return true;
    }

    @Override
    public PageDto<AnswerParameterDto> getParametersByAnswerId(Long answerId, int page, int pageSize) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(()->new EntityNotFoundException("Answer with id " + answerId + " not found"));

        Page<AnswerParameter> result = answerParameterRepository.findAllByAnswer(answer, PagesUtility.createPageableUnsorted(page, pageSize));
        return PageDto.of(result.getTotalElements(), page, mapToDto(result.getContent()));
    }

    private List<AnswerParameterDto> mapToDto(List<AnswerParameter> answers) {
        return answers.stream()
                .map(AnswerParameterMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }
}
