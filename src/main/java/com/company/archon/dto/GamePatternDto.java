package com.company.archon.dto;

import com.company.archon.entity.Game;
import com.company.archon.entity.Parameter;
import com.company.archon.entity.Question;
import com.company.archon.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
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
public class GamePatternDto extends BaseDto {

    private String title;

    private Integer usersAmount;


//    private List<QuestionDto> questions;
//
//    private List<GameDto> games;

}
