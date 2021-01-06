package com.company.archon.services;

import com.company.archon.dto.AnswerParameterDto;

import java.util.List;

public interface AnswerParameterService {

    boolean update(Long parameterId, Integer value);

    List<AnswerParameterDto> getParametersByAnswerId(Long id);
}
