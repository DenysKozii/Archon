package com.company.archon.repositories;

import com.company.archon.entity.Question;
import com.company.archon.entity.QuestionParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface QuestionParameterRepository extends JpaRepository<QuestionParameter, Long>, PagingAndSortingRepository<QuestionParameter, Long> {
    List<QuestionParameter> findAllByQuestion(Question question);

    Page<QuestionParameter> findAllByQuestion(Question question, Pageable pageable);
}
