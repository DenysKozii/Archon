package com.company.archon.repositories;

import com.company.archon.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GameParameterRepository extends JpaRepository<GameParameter, Long> {
    List<GameParameter> findAllByGame(Game game);


    Optional<GameParameter> findAllByTitleAndGame(String title, Game game);
}
