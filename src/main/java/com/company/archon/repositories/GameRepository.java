package com.company.archon.repositories;

import com.company.archon.entity.Game;
import com.company.archon.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Optional;

public interface GameRepository extends JpaRepository<Game, Long>, PagingAndSortingRepository<Game, Long> {
    Page<Game> findByUser(User user, Pageable pageable);

    Optional<Game> findById(Long id);

}
