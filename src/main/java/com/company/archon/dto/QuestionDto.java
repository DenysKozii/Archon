package com.company.archon.dto;

import com.company.archon.enums.GameStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.company.archon.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class QuestionDto extends BaseDto {

    private String title;

    private String context;

    private GameStatus status;

    private Image image;

    private Integer weight;

    private List<QuestionParameterDto> questionParameters;

    private List<QuestionUserParameterDto> questionUserParameters;

}
