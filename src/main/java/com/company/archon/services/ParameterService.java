package com.company.archon.services;


import com.company.archon.dto.ParameterDto;
import com.company.archon.pagination.PageDto;

public interface ParameterService {

    PageDto<ParameterDto> getParametersByGamePatternId(Long gamePatternId);

    boolean deleteById(Long parameterId);

    boolean create(ParameterDto parameterDto);

    boolean create(String title, Integer defaultValue, Integer highestValue, Integer lowestValue, Boolean visible, Long gamePatternId);
}
