package com.company.archon.repositories;

import com.company.archon.entity.Question;
import com.company.archon.entity.QuestionParameter;
import com.company.archon.entity.QuestionUserParameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface QuestionUserParameterRepository extends JpaRepository<QuestionUserParameter, Long> {

    List<QuestionUserParameter> findAllByQuestion(Question question);

}