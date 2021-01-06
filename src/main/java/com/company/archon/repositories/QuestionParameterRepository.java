package com.company.archon.repositories;

import com.company.archon.entity.Game;
import com.company.archon.entity.Question;
import com.company.archon.entity.QuestionParameter;
import com.company.archon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface QuestionParameterRepository extends JpaRepository<QuestionParameter, Long> {
    List<QuestionParameter> findAllByQuestion(Question question);
}
