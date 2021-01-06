package com.company.archon.mapper;

import com.company.archon.dto.AnswerDto;
import com.company.archon.dto.QuestionDto;
import com.company.archon.entity.Answer;
import com.company.archon.entity.Question;
import liquibase.pro.packaged.A;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AnswerMapper {
    AnswerMapper INSTANCE = Mappers.getMapper(AnswerMapper.class);

//    @BeanMapping( ignoreUnmappedSourceProperties={"relativeQuestions", "question"} )
    AnswerDto mapToDto(Answer answer);
}
