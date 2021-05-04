package com.company.archon.services;


import com.company.archon.dto.ConditionParameterDto;
import com.company.archon.dto.ParameterDto;

import java.util.List;

public interface ConditionParameterService {
    List<ConditionParameterDto> getParametersByGamePatternId(Long gamePatternId);

    void updateStart(Integer valueStart, Long parameterId);

}
