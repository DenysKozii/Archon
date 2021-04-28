package com.company.archon.mapper;

import com.company.archon.dto.GameRequestDto;
import com.company.archon.entity.GameRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface GameRequestMapper {
    GameRequestMapper INSTANCE = Mappers.getMapper(GameRequestMapper.class);

    GameRequestDto mapToDto(GameRequest gameRequest);
}
