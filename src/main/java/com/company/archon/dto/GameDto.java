package com.company.archon.dto;

import com.company.archon.entity.GameParameter;
import com.company.archon.entity.GamePattern;
import com.company.archon.entity.Question;
import com.company.archon.entity.User;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class GameDto extends BaseDto{


//    private Set<UserDto> users;
//
//    private List<QuestionDto> questionsPull;

    private GamePatternDto gamePattern;

//    private List<GameParameterDto> parameters;

}
