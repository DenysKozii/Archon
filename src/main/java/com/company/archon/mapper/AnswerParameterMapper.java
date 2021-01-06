package com.company.archon.mapper;

import com.company.archon.dto.AnswerParameterDto;
import com.company.archon.dto.GameParameterDto;
import com.company.archon.entity.AnswerParameter;
import com.company.archon.entity.GameParameter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AnswerParameterMapper {
    AnswerParameterMapper INSTANCE = Mappers.getMapper(AnswerParameterMapper.class);

    AnswerParameterDto mapToDto(AnswerParameter answerParameter);
}
