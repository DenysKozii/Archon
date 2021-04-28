package com.company.archon.services;

import com.company.archon.dto.UserDto;
import com.company.archon.dto.user.LoginRequest;
import com.company.archon.dto.user.UserProfileDto;
import com.company.archon.pagination.PageDto;

public interface UserService {

    UserProfileDto loginUser(LoginRequest request);

    boolean addUser(UserDto user);

    PageDto<String> getFriendsByUsername(int page, int pageSize);

}
