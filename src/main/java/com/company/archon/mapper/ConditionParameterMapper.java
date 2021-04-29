package com.company.archon.mapper;

import com.company.archon.dto.ConditionParameterDto;
import com.company.archon.entity.ConditionParameter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ConditionParameterMapper {
    ConditionParameterMapper INSTANCE = Mappers.getMapper(ConditionParameterMapper.class);

    ConditionParameterDto mapToDto(ConditionParameter conditionParameter);
}
