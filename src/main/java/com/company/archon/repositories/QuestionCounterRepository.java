package com.company.archon.repositories;

import com.company.archon.entity.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface QuestionCounterRepository extends JpaRepository<QuestionCounter, Long>{

    Optional<QuestionCounter> findByQuestionAndUserId(Question question, Long userId);

}
