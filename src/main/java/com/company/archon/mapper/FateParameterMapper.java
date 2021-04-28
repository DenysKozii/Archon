package com.company.archon.mapper;

import com.company.archon.dto.ParameterDto;
import com.company.archon.entity.Parameter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface FateParameterMapper {
    FateParameterMapper INSTANCE = Mappers.getMapper(FateParameterMapper.class);

    ParameterDto mapToDto(Parameter parameter);
}
