package com.company.archon.repositories;

import com.company.archon.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findById(Long id);

    List<Answer> findAllByQuestionId(Long id);
}