package com.company.archon.services.impl;

import com.company.archon.dto.ParameterDto;
import com.company.archon.entity.AnswerParameter;
import com.company.archon.entity.BaseEntity;
import com.company.archon.entity.GamePattern;
import com.company.archon.entity.Parameter;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.mapper.FateParameterMapper;
import com.company.archon.pagination.PageDto;
import com.company.archon.pagination.PagesUtility;
import com.company.archon.repositories.GamePatternRepository;
import com.company.archon.repositories.ParameterRepository;
import com.company.archon.services.ParameterService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional
@AllArgsConstructor
public class ParameterServiceImpl implements ParameterService {

    private final ParameterRepository parameterRepository;
    private final GamePatternRepository gamePatternRepository;

    @Override
    public PageDto<ParameterDto> getParametersByGamePatternId(Long gamePatternId) {
        Page<Parameter> result = parameterRepository.findAllByGamePatternId(gamePatternId, PagesUtility.createPageableUnsorted(0, 150));
        List<Parameter> parameters = result.getContent().stream()
                .sorted(Comparator.comparingLong(BaseEntity::getId))
                .collect(Collectors.toList());
        return PageDto.of(result.getTotalElements(), 0, mapToDto(parameters));
    }

    private List<ParameterDto> mapToDto(List<Parameter> parameters) {
        return parameters.stream()
                .map(FateParameterMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean create(ParameterDto parameterDto) {
        GamePattern gamePattern = gamePatternRepository.findById(parameterDto.getGamePatternId())
                .orElseThrow(()->new EntityNotFoundException("GamePattern with id " + parameterDto.getGamePatternId() + " not found"));
        Parameter parameter = new Parameter();
        parameter.setVisible(parameterDto.getVisible());
        parameter.setTitle(parameterDto.getTitle());
        parameter.setDefaultValue(parameterDto.getDefaultValue());
        parameter.setLowestValue(parameterDto.getLowestValue());
        parameter.setHighestValue(parameterDto.getHighestValue());
        parameter.setGamePattern(gamePattern);
        parameterRepository.save(parameter);
        return true;
    }

    @Override
    public boolean create(String title, Integer defaultValue, Integer highestValue, Integer lowestValue, Boolean visible, Long gamePatternId) {
        GamePattern gamePattern = gamePatternRepository.findById(gamePatternId)
                .orElseThrow(()->new EntityNotFoundException("GamePattern with id " + gamePatternId + " not found"));
        Parameter parameter = new Parameter();
        parameter.setVisible(visible);
        parameter.setTitle(title);
        parameter.setDefaultValue(defaultValue);
        parameter.setLowestValue(lowestValue);
        parameter.setHighestValue(highestValue);
        parameter.setGamePattern(gamePattern);
        parameterRepository.save(parameter);
        return true;
    }


    @Override
    public boolean deleteById(Long parameterId) {
        Parameter parameter = parameterRepository.findById(parameterId)
                .orElseThrow(()->new EntityNotFoundException("Parameter with id " + parameterId + " not found"));
//        Optional<GamePattern> gamePatternOptional = gamePatternRepository.findByParametersContaining(parameter);
//        if (gamePatternOptional.isPresent()){
//            gamePatternOptional.get().getParameters().remove(parameter);
//            gamePatternRepository.save(gamePatternOptional.get());
//        }
        parameterRepository.delete(parameter);
        return true;
    }
}
