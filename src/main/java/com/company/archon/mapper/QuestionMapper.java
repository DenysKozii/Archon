package com.company.archon.mapper;

import com.company.archon.dto.QuestionDto;
import com.company.archon.entity.Question;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    QuestionMapper INSTANCE = Mappers.getMapper(QuestionMapper.class);

//    @BeanMapping( ignoreUnmappedSourceProperties={"question_conditions","answers","relative_answers"} )
    QuestionDto mapToDto(Question game);
}
