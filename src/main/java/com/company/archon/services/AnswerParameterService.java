package com.company.archon.services;


import com.company.archon.dto.AnswerParameterDto;
import com.company.archon.dto.AnswerUserParameterDto;
import com.company.archon.pagination.PageDto;

import java.util.List;

public interface AnswerParameterService {

    boolean update(Long parameterId, Integer value);

    PageDto<AnswerParameterDto> getParametersByAnswerId(Long id, int page, int pageSize);

    List<AnswerUserParameterDto> getUserParametersByAnswerId(Long id);

    void updateUserParameter(Long parameterId, Integer influence);
}
