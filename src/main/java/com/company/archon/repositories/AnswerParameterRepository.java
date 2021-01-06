package com.company.archon.repositories;

import com.company.archon.entity.Answer;
import com.company.archon.entity.AnswerParameter;
import com.company.archon.entity.Game;
import com.company.archon.entity.GameParameter;
import liquibase.pro.packaged.S;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerParameterRepository extends JpaRepository<AnswerParameter, Long> {
    List<AnswerParameter> findAllByAnswer(Answer answer);

    Optional<AnswerParameter> findByTitleAndAnswer(String title, Answer answer);
}
