package com.company.archon.repositories;

import com.company.archon.entity.Answer;
import com.company.archon.entity.AnswerParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface AnswerParameterRepository extends JpaRepository<AnswerParameter, Long>, PagingAndSortingRepository<AnswerParameter, Long> {
    Page<AnswerParameter> findAllByAnswer(Answer answer, Pageable pageable);

    Optional<AnswerParameter> findByTitleAndAnswer(String title, Answer answer);
}
