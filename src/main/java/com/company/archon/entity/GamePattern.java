package com.company.archon.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "game_patterns")
public class GamePattern extends BaseEntity {

    private Long orderId;

    @NotNull
    private String title;

    @NotNull
    private Integer usersAmount;

    private Boolean deleted;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "game_pattern_user",
            joinColumns = @JoinColumn(name = "game_pattern_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users = new HashSet<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, mappedBy = "gamePattern")
    private List<Question> questions = new ArrayList<>();

    @Transient
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "gamePattern")
    private List<Game> games = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, cascade=CascadeType.ALL, mappedBy = "gamePattern")
    private List<Parameter> parameters = new ArrayList<>();

}
