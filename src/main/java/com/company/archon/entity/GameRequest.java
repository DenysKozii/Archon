package com.company.archon.entity;

import com.company.archon.enums.GameRequestStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "game_request")
@NoArgsConstructor
@AllArgsConstructor
public class GameRequest extends BaseEntity{

    private String invitorUsername;

    private String  acceptorUsername;

    private Long gamePatternId;

    private GameRequestStatus status;


}
