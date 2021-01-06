package com.company.archon.services;

import com.company.archon.dto.QuestionParameterDto;

import java.util.List;

public interface QuestionParameterService {

    List<QuestionParameterDto> getParametersByQuestionId(Long questionId);

    boolean update(Long parameterId, Integer appear, Integer disappear);
}
