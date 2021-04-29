package com.company.archon.repositories;

import com.company.archon.entity.ConditionParameter;
import com.company.archon.entity.GamePattern;
import com.company.archon.entity.Parameter;
import com.company.archon.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface GamePatternRepository extends JpaRepository<GamePattern, Long>, PagingAndSortingRepository<GamePattern, Long> {

    Page<GamePattern> findAll(Pageable pageable);

    List<GamePattern> findAll();

    Optional<GamePattern> findById(Long id);

    Optional<GamePattern> findByTitle(String title);

    Optional<GamePattern> findByParametersContaining(Parameter parameter);

    Optional<GamePattern> findByConditionParametersContaining(ConditionParameter parameter);
}
