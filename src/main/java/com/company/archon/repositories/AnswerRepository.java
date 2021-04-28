package com.company.archon.repositories;

import com.company.archon.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long>, PagingAndSortingRepository<Answer, Long> {
    Optional<Answer> findById(Long id);

    List<Answer> findAllByQuestionId(Long id);

    Page<Answer> findAllByQuestionId(Long id, Pageable pageable);
}