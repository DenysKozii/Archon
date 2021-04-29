package com.company.archon.services;


import com.company.archon.dto.QuestionParameterDto;
import com.company.archon.dto.QuestionUserParameterDto;
import com.company.archon.pagination.PageDto;

import java.util.List;

public interface QuestionParameterService {

    PageDto<QuestionParameterDto> getParametersByQuestionId(Long questionId, int page, int pageSize);

    boolean update(Long parameterId, Integer appear, Integer disappear);

    List<QuestionUserParameterDto> getUserParametersByQuestionId(Long questionId);

    void updateUserParameter(Long parameterId, Integer appear, Integer disappear);
}
