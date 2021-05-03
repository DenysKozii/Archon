package com.company.archon.services.impl;

import com.company.archon.dto.AnswerParameterDto;
import com.company.archon.dto.AnswerUserParameterDto;
import com.company.archon.dto.BaseDto;
import com.company.archon.entity.*;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.mapper.AnswerParameterMapper;
import com.company.archon.mapper.AnswerUserParameterMapper;
import com.company.archon.pagination.PageDto;
import com.company.archon.pagination.PagesUtility;
import com.company.archon.repositories.*;
import com.company.archon.services.AnswerParameterService;
import com.company.archon.services.AnswerService;
import com.company.archon.services.AuthorizationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class AnswerParameterServiceImpl implements AnswerParameterService {

    private final AnswerParameterRepository answerParameterRepository;
    private final AnswerRepository answerRepository;
    private final AnswerUserParameterRepository answerUserParameterRepository;
    private final UserParameterRepository userParameterRepository;
    private final UserRepository userRepository;
    private final AuthorizationService authorizationService;

    @Override
    public boolean update(Long parameterId, Integer value) {
        AnswerParameter answerParameter = answerParameterRepository.findById(parameterId)
                .orElseThrow(()->new EntityNotFoundException("AnswerParameter with id " + parameterId + " not found"));
        answerParameter.setValue(value);
        answerParameterRepository.save(answerParameter);
        return true;
    }

    @Override
    public void updateUserParameter(Long parameterId, Integer influence) {
        AnswerUserParameter answerUserParameter = answerUserParameterRepository.findById(parameterId)
                .orElseThrow(()->new EntityNotFoundException("AnswerUserParameter with id " + parameterId + " not found"));
        answerUserParameter.setValue(influence);
        answerUserParameterRepository.save(answerUserParameter);
    }

    @Override
    public PageDto<AnswerParameterDto> getParametersByAnswerId(Long answerId, int page, int pageSize) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(()->new EntityNotFoundException("Answer with id " + answerId + " not found"));

        Page<AnswerParameter> result = answerParameterRepository.findAllByAnswer(answer, PagesUtility.createPageableUnsorted(page, pageSize));
        List<AnswerParameter> parameters = result.getContent().stream()
                .sorted(Comparator.comparingLong(BaseEntity::getId))
                .collect(Collectors.toList());
        return PageDto.of(result.getTotalElements(), page, mapToDto(parameters));
    }

    @Override
    public List<AnswerUserParameterDto> getUserParametersByAnswerId(Long answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(()->new EntityNotFoundException("Answer with id " + answerId + " not found"));
        return answerUserParameterRepository.findAllByAnswer(answer).stream()
                .map(AnswerUserParameterMapper.INSTANCE::mapToDto)
                .sorted(Comparator.comparing(BaseDto::getId))
                .collect(Collectors.toList());
    }

    private List<AnswerParameterDto> mapToDto(List<AnswerParameter> answers) {
        return answers.stream()
                .map(AnswerParameterMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }
}
