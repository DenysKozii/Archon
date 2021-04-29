package com.company.archon.services;

import com.company.archon.dto.UserDto;
import com.company.archon.dto.UserParameterDto;
import com.company.archon.dto.user.LoginRequest;
import com.company.archon.dto.user.UserProfileDto;
import com.company.archon.pagination.PageDto;

import java.util.List;

public interface UserService {

    UserProfileDto loginUser(LoginRequest request);

    boolean addUser(UserDto user);

    PageDto<String> getFriendsByUsername(int page, int pageSize);

    List<UserParameterDto> findParameters(String username);

    boolean updateParameter(Long parameterId, Integer value);
}
