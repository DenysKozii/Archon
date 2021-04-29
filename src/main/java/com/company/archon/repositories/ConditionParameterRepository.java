package com.company.archon.repositories;

import com.company.archon.entity.ConditionParameter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConditionParameterRepository extends JpaRepository<ConditionParameter, Long> {
    List<ConditionParameter> findAllByGamePatternId(Long gamePatternId);
}
