package com.company.archon.services;

import com.company.archon.dto.GameDto;
import com.company.archon.dto.ParameterDto;
import com.company.archon.dto.QuestionDto;
import com.company.archon.entity.User;

import java.util.List;

public interface ParameterService {

    List<ParameterDto> getParametersByGamePatternId(Long gamePatternId);


    boolean deleteParameter(Long parameterId);

    boolean createParameter(String title, Integer defaultValue, Integer highestValue, Integer lowestValue, Long gamePatternId);
}
