package com.company.archon.repositories;

import com.company.archon.entity.User;
import com.company.archon.entity.UserParameter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserParameterRepository extends JpaRepository<UserParameter, Long> {

    List<UserParameter> findAllByUser(User user);

    Optional<UserParameter> findByTitleAndUser(String title, User user);

}
