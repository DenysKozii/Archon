package com.company.archon.services;


import com.company.archon.dto.QuestionParameterDto;
import com.company.archon.pagination.PageDto;

public interface QuestionParameterService {

    PageDto<QuestionParameterDto> getParametersByQuestionId(Long questionId, int page, int pageSize);

    boolean update(Long parameterId, Integer appear, Integer disappear);
}
