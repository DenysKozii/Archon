package com.company.archon.services.impl;


import com.company.archon.entity.FriendRequest;
import com.company.archon.entity.User;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.repositories.FriendRequestRepository;
import com.company.archon.repositories.UserRepository;
import com.company.archon.services.FriendRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FriendRequestServiceImpl implements FriendRequestService {

    private final FriendRequestRepository friendRequestRepository;
    private final UserRepository userRepository;

    @Autowired
    public FriendRequestServiceImpl(FriendRequestRepository friendRequestRepository, UserRepository userRepository) {
        this.friendRequestRepository = friendRequestRepository;
        this.userRepository = userRepository;
    }

    @Override
    public boolean inviteByEmail(String userEmail, String friendEmail) {
        Optional<FriendRequest> friendRequestOptional = friendRequestRepository.findByInvitorEmailAndAcceptorEmail(userEmail, friendEmail);

        if (friendRequestOptional.isEmpty()) {
            FriendRequest friendRequest = new FriendRequest();
            friendRequest.setInvitorEmail(userEmail);
            friendRequest.setAcceptorEmail(friendEmail);
            friendRequest.setStatus(true);
            friendRequestRepository.save(friendRequest);
            return true;
        }
        return false;
    }

    @Override
    public boolean acceptByEmail(String userEmail, String friendEmail) {
        FriendRequest friendRequest = friendRequestRepository.findByInvitorEmailAndAcceptorEmail(userEmail, friendEmail)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Friend request by user email %s and friend email %s doesn't exists!", userEmail, friendEmail)));
        if (!friendRequest.getStatus())
            return false;
        friendRequest.setStatus(false);
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + userEmail + " doesn't exists!"));
        User friend = userRepository.findByEmail(friendEmail)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + friendEmail + " doesn't exists!"));
        user.getFriends().add(friend);
        friend.getFriends().add(user);
        userRepository.save(user);
        userRepository.save(friend);
        friendRequestRepository.save(friendRequest);
        return true;
    }


    @Override
    public boolean deleteByEmail(String userEmail, String friendEmail) {
        FriendRequest friendRequest = friendRequestRepository.findByInvitorEmailAndAcceptorEmail(userEmail, friendEmail)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Friend request by user email %s and friend email %s doesn't exists!", userEmail, friendEmail)));
        friendRequestRepository.delete(friendRequest);

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + userEmail + " doesn't exists!"));
        User friend = userRepository.findByEmail(friendEmail)
                .orElseThrow(() -> new EntityNotFoundException("User with email " + friendEmail + " doesn't exists!"));

        user.getFriends().remove(friend);
        friend.getFriends().remove(user);
        userRepository.save(user);
        userRepository.save(friend);
        return true;
    }

    @Override
    public List<String> acceptList(String email) {
        return friendRequestRepository.findAllByAcceptorEmail(email).stream()
                .filter(FriendRequest::getStatus)
                .map(FriendRequest::getInvitorEmail)
                .collect(Collectors.toList());

    }

    @Override
    public List<String> inviteList(String email) {
        return friendRequestRepository.findAllByInvitorEmail(email).stream()
                .filter(FriendRequest::getStatus)
                .map(FriendRequest::getAcceptorEmail)
                .collect(Collectors.toList());
    }

}