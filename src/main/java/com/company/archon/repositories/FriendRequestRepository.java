package com.company.archon.repositories;

import com.company.archon.entity.FriendRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
    Optional<FriendRequest> findByInvitorEmailAndAcceptorEmail(String invitorEmail, String acceptorEmail);

    List<FriendRequest> findAllByAcceptorEmail(String email);

    List<FriendRequest> findAllByInvitorEmail(String email);
}
