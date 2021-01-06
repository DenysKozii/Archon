package com.company.archon.dto;

import com.company.archon.entity.Game;
import com.company.archon.entity.Parameter;
import com.company.archon.entity.Question;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GameParameterDto extends BaseDto {

    private String title;

    private Integer value;

    private ParameterDto parameter;

    private GameDto game;
}
