package com.company.archon.mapper;

import com.company.archon.dto.AnswerParameterDto;
import com.company.archon.dto.AnswerUserParameterDto;
import com.company.archon.entity.AnswerParameter;
import com.company.archon.entity.AnswerUserParameter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AnswerUserParameterMapper {
    AnswerUserParameterMapper INSTANCE = Mappers.getMapper(AnswerUserParameterMapper.class);

    AnswerUserParameterDto mapToDto(AnswerUserParameter answerUserParameter);
}
