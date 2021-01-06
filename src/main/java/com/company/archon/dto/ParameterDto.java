package com.company.archon.dto;

import com.company.archon.entity.Game;
import com.company.archon.entity.GameParameter;
import com.company.archon.entity.GamePattern;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(value = PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ParameterDto extends BaseDto {

    private String title;

    private Integer defaultValue;

    private Integer highestValue;

    private Integer lowestValue;

    private GamePatternDto gamePattern;

}
