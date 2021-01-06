package com.company.archon.repositories;

import com.company.archon.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParameterRepository extends JpaRepository<Parameter, Long> {

    List<Parameter> findAllByGamePattern(GamePattern gamePattern);

}
