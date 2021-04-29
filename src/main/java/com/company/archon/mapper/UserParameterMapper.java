package com.company.archon.mapper;

import com.company.archon.dto.UserParameterDto;
import com.company.archon.entity.UserParameter;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserParameterMapper {
    UserParameterMapper INSTANCE = Mappers.getMapper(UserParameterMapper.class);

    UserParameterDto mapToDto(UserParameter userParameter);
}
