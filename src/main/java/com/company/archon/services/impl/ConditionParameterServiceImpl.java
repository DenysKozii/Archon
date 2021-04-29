package com.company.archon.services.impl;

import com.company.archon.dto.ConditionParameterDto;
import com.company.archon.dto.ParameterDto;
import com.company.archon.entity.ConditionParameter;
import com.company.archon.entity.GamePattern;
import com.company.archon.entity.Parameter;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.mapper.ConditionParameterMapper;
import com.company.archon.mapper.FateParameterMapper;
import com.company.archon.pagination.PageDto;
import com.company.archon.pagination.PagesUtility;
import com.company.archon.repositories.ConditionParameterRepository;
import com.company.archon.repositories.GamePatternRepository;
import com.company.archon.repositories.ParameterRepository;
import com.company.archon.services.ConditionParameterService;
import com.company.archon.services.ParameterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class ConditionParameterServiceImpl implements ConditionParameterService {

    private final ConditionParameterRepository conditionParameterRepository;

    @Override
    public List<ConditionParameterDto> getParametersByGamePatternId(Long gamePatternId) {
        List<ConditionParameter> result = conditionParameterRepository.findAllByGamePatternId(gamePatternId);
        return result.stream()
                .map(ConditionParameterMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateStart(Integer valueStart, Long parameterId) {
        ConditionParameter conditionParameter = conditionParameterRepository.findById(parameterId)
                .orElseThrow(() -> new EntityNotFoundException("ConditionParameter with id " + parameterId + " not found"));
        conditionParameter.setValueStart(valueStart);
        conditionParameterRepository.save(conditionParameter);
    }

    @Override
    public void updateFinish(Integer valueFinish, Long parameterId) {
        ConditionParameter conditionParameter = conditionParameterRepository.findById(parameterId)
                .orElseThrow(() -> new EntityNotFoundException("ConditionParameter with id " + parameterId + " not found"));
        conditionParameter.setValueFinish(valueFinish);
        conditionParameterRepository.save(conditionParameter);
    }

}
