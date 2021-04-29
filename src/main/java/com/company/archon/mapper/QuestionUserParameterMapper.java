package com.company.archon.mapper;

import com.company.archon.dto.QuestionParameterDto;
import com.company.archon.dto.QuestionUserParameterDto;
import com.company.archon.entity.QuestionParameter;
import com.company.archon.entity.QuestionUserParameter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface QuestionUserParameterMapper {
    QuestionUserParameterMapper INSTANCE = Mappers.getMapper(QuestionUserParameterMapper.class);

    QuestionUserParameterDto mapToDto(QuestionUserParameter questionUserParameter);
}
