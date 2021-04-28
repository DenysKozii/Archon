package com.company.archon.mapper;

import com.company.archon.dto.AnswerDto;
import com.company.archon.entity.Answer;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AnswerMapper {
    AnswerMapper INSTANCE = Mappers.getMapper(AnswerMapper.class);

//    @BeanMapping( ignoreUnmappedSourceProperties={"relativeQuestions", "question"} )
    AnswerDto mapToDto(Answer answer);
}
