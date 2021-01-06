package com.company.archon.dto;

import com.company.archon.entity.*;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class QuestionDto extends BaseDto {

    private String title;

    private String context;

    private String image;


//    private List<QuestionDto> questionConditions;
//
//    private List<AnswerDto> answers;

    private GamePatternDto gamePattern;

    private GameDto game;

//    private List<AnswerDto> relative_answers;
//
//    private List<QuestionParameterDto> questionParameters;

    private Integer weight;

}
