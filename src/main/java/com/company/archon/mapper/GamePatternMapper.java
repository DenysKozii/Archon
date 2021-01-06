package com.company.archon.mapper;

import com.company.archon.dto.GamePatternDto;
import com.company.archon.entity.GamePattern;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface GamePatternMapper {
    GamePatternMapper INSTANCE = Mappers.getMapper(GamePatternMapper.class);

    GamePatternDto mapToDto(GamePattern gamePattern);
}
