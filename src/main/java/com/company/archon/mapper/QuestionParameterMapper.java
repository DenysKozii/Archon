package com.company.archon.mapper;

import com.company.archon.dto.QuestionParameterDto;
import com.company.archon.entity.QuestionParameter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface QuestionParameterMapper {
    QuestionParameterMapper INSTANCE = Mappers.getMapper(QuestionParameterMapper.class);

    QuestionParameterDto mapToDto(QuestionParameter questionParameter);
}
