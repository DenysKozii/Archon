package com.company.archon.repositories;

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

    List<GamePattern> findByUsers(User user);

    Page<GamePattern> findAll(Pageable pageable);

    Optional<GamePattern> findById(Long id);

    Optional<GamePattern> findByTitle(String title);

    Optional<GamePattern> findByParametersContaining(Parameter parameter);
}
