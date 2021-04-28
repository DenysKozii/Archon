package com.company.archon.repositories;

import com.company.archon.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long>, PagingAndSortingRepository<Question, Long> {

    List<Question> findAllByGamePatternId(Long id);

    Page<Question> findAllByGamePatternId(Long id, Pageable pageable);

}