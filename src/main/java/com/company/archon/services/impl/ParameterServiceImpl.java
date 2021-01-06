package com.company.archon.services.impl;

import com.company.archon.dto.ParameterDto;
import com.company.archon.entity.GamePattern;
import com.company.archon.entity.Parameter;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.mapper.ParameterMapper;
import com.company.archon.repositories.GamePatternRepository;
import com.company.archon.repositories.ParameterRepository;
import com.company.archon.services.ParameterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
public class ParameterServiceImpl implements ParameterService {
    private final ParameterRepository parameterRepository;
    private final GamePatternRepository gamePatternRepository;

    @Autowired
    public ParameterServiceImpl(ParameterRepository parameterRepository, GamePatternRepository gamePatternRepository) {
        this.parameterRepository = parameterRepository;
        this.gamePatternRepository = gamePatternRepository;
    }

    @Override
    public List<ParameterDto> getParametersByGamePatternId(Long gamePatternId) {
        GamePattern gamePattern = gamePatternRepository.findById(gamePatternId)
                .orElseThrow(()->new EntityNotFoundException("GamePattern with id " + gamePatternId + " not found"));
        return parameterRepository.findAllByGamePattern(gamePattern).stream()
                .map(ParameterMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean createParameter(String title, Integer defaultValue, Integer highestValue, Integer lowestValue, Long gamePatternId) {
        GamePattern gamePattern = gamePatternRepository.findById(gamePatternId)
                .orElseThrow(()->new EntityNotFoundException("GamePattern with id " + gamePatternId + " not found"));
        Parameter parameter = new Parameter();
        parameter.setTitle(title);
        parameter.setDefaultValue(defaultValue);
        parameter.setLowestValue(lowestValue);
        parameter.setHighestValue(highestValue);
        parameter.setGamePattern(gamePattern);
        parameterRepository.save(parameter);
        return true;
    }


    @Override
    public boolean deleteParameter(Long parameterId) {
        Parameter parameter = parameterRepository.findById(parameterId)
                .orElseThrow(()->new EntityNotFoundException("Parameter with id " + parameterId + " not found"));
        parameterRepository.delete(parameter);
        return true;
    }
}
