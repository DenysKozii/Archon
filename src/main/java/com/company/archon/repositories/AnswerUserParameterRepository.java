package com.company.archon.repositories;

import com.company.archon.entity.Answer;
import com.company.archon.entity.AnswerParameter;
import com.company.archon.entity.AnswerUserParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerUserParameterRepository extends JpaRepository<AnswerUserParameter, Long> {
    List<AnswerUserParameter> findAllByAnswer(Answer answer);

    Optional<AnswerUserParameter> findByTitleAndAnswer(String title, Answer answer);
}
