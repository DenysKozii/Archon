package com.company.archon.dto;

import com.company.archon.entity.AnswerParameter;
import com.company.archon.entity.Question;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class AnswerDto extends BaseDto{
    private String context;

    private QuestionDto question;

//    private List<QuestionDto> relativeQuestions;

    private List<AnswerParameterDto> parameters;

}
