package com.company.archon.mapper;

import com.company.archon.dto.GameParameterDto;
import com.company.archon.dto.QuestionParameterDto;
import com.company.archon.entity.GameParameter;
import com.company.archon.entity.QuestionParameter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface GameParameterMapper {
    GameParameterMapper INSTANCE = Mappers.getMapper(GameParameterMapper.class);

    GameParameterDto mapToDto(GameParameter gameParameter);
}
