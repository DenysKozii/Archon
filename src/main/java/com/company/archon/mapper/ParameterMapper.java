package com.company.archon.mapper;

import com.company.archon.dto.ParameterDto;
import com.company.archon.dto.QuestionDto;
import com.company.archon.entity.Parameter;
import com.company.archon.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ParameterMapper {
    ParameterMapper INSTANCE = Mappers.getMapper(ParameterMapper.class);

    ParameterDto mapToDto(Parameter parameter);
}
