package com.company.archon.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "question_user_parameters")
public class QuestionUserParameter extends BaseEntity {

    private String title;

    private Integer valueAppear;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_question")
    private Question question;
}
