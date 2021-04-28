package com.company.archon.mapper;

import com.company.archon.dto.AnswerParameterDto;
import com.company.archon.entity.AnswerParameter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AnswerParameterMapper {
    AnswerParameterMapper INSTANCE = Mappers.getMapper(AnswerParameterMapper.class);

    AnswerParameterDto mapToDto(AnswerParameter answerParameter);
}
