package com.company.archon.services;

import com.company.archon.dto.UserDto;
import com.company.archon.entity.User;

import java.util.List;

public interface UserService {

    boolean addUser(User user);

    List<String> getFriendsByUserEmail(String email);

}
