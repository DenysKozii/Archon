package com.company.archon.services.impl;

import com.company.archon.dto.UserDto;
import com.company.archon.dto.user.LoginRequest;
import com.company.archon.dto.user.UserProfileDto;
import com.company.archon.entity.Role;
import com.company.archon.entity.User;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.pagination.PageDto;
import com.company.archon.pagination.PagesUtility;
import com.company.archon.repositories.UserRepository;
import com.company.archon.services.AuthorizationService;
import com.company.archon.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final AuthorizationService authorizationService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final String PASSWORD = "GDJ1231@#";

    @Override
    public UserProfileDto loginUser(LoginRequest request) {
        String email = request.getUsername();
        User user = userRepository.findByUsername(email).orElseThrow(() ->
                new UsernameNotFoundException("Invalid Credentials"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new EntityNotFoundException("Invalid Credentials");
        }
        authorizationService.authorizeUser(user);
        return map(user);
    }

    private UserProfileDto map(User user) {
        return new UserProfileDto(
                user.getId(),
                user.getUsername()
        );
    }

    public boolean addUser(UserDto userDto) {
        Optional<User> userFromDb = userRepository.findByUsername(userDto.getUsername());
        if (userFromDb.isPresent()) {
            User user = userRepository.findByUsername(userDto.getUsername()).orElseThrow(() ->
                    new UsernameNotFoundException("Invalid Credentials"));
            authorizationService.authorizeUser(user);
            return true;
        } else{
            User user = new User();
            user.setRole(Role.USER);
            user.setUsername(userDto.getUsername());
            user.setPassword(passwordEncoder.encode(PASSWORD));
            userRepository.save(user);
            authorizationService.authorizeUser(user);
        }
        return true;
    }

    @Override
    public PageDto<String> getFriendsByUsername(int page, int pageSize) {
        String username = authorizationService.getProfileOfCurrent().getUsername();
        Page<User> result = userRepository.findFriendsByUsername(username, PagesUtility.createPageableUnsorted(page, pageSize));
        return PageDto.of(result.getTotalElements(), page, mapToDto(result.getContent()));
    }

    private List<String> mapToDto(List<User> users) {
        return users.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

}
