package com.company.archon.repositories;

import com.company.archon.entity.GamePattern;
import com.company.archon.entity.Parameter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ParameterRepository extends JpaRepository<Parameter, Long>, PagingAndSortingRepository<Parameter, Long> {

    List<Parameter> findAllByGamePatternId(Long gamePatternId);

    Page<Parameter> findAllByGamePatternId(Long gamePatternId, Pageable pageable);

    List<Parameter> findAllByGamePattern(GamePattern gamePattern);
}
