package com.company.archon.entity;

import com.company.archon.enums.GameStatus;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "questions")
public class Question extends BaseEntity{
    @NonNull
    private String title;
    @NonNull
    private String context;

    private GameStatus status = GameStatus.RUNNING;

    @Lob
    @Column(columnDefinition = "MEDIUMBLOB")
    private String image;

    @Transient
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    private List<QuestionCounter> questionCounters = new ArrayList<>();


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "question_conditions",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "condition_id", referencedColumnName = "id"))
    private List<Question> questionConditions = new ArrayList<>();

    @Transient
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    private List<Answer> answers = new ArrayList<>();


    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name="game_pattern")
    private GamePattern gamePattern;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    private List<QuestionParameter> questionParameters = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "question")
    private List<QuestionUserParameter> questionUserParameters = new ArrayList<>();

    private Integer weight;

}
