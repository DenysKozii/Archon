package com.company.archon.services;


import com.company.archon.dto.AnswerParameterDto;
import com.company.archon.pagination.PageDto;

public interface AnswerParameterService {

    boolean update(Long parameterId, Integer value);

    PageDto<AnswerParameterDto> getParametersByAnswerId(Long id, int page, int pageSize);
}
