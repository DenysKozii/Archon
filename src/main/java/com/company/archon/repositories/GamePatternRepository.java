package com.company.archon.repositories;

import com.company.archon.entity.Game;
import com.company.archon.entity.GamePattern;
import com.company.archon.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GamePatternRepository extends JpaRepository<GamePattern, Long> {

    List<GamePattern> findByUsers(User user);

    Optional<GamePattern> findById(Long id);

    Optional<GamePattern> findByTitle(String title);
}
