package com.company.archon.services;

import java.util.List;

public interface FriendRequestService {
    boolean inviteByEmail(String userEmail, String friendEmail);

    boolean acceptByEmail(String userEmail, String friendEmail);

    boolean deleteByEmail(String userEmail, String friendEmail);

    List<String> acceptList(String email);

    List<String> inviteList(String email);

}