package com.company.archon.services.impl;

import com.company.archon.dto.UserDto;
import com.company.archon.dto.UserParameterDto;
import com.company.archon.dto.user.LoginRequest;
import com.company.archon.dto.user.UserProfileDto;
import com.company.archon.entity.Role;
import com.company.archon.entity.User;
import com.company.archon.entity.UserParameter;
import com.company.archon.exception.EntityNotFoundException;
import com.company.archon.mapper.UserParameterMapper;
import com.company.archon.pagination.PageDto;
import com.company.archon.pagination.PagesUtility;
import com.company.archon.repositories.UserParameterRepository;
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
    private final UserParameterRepository userParameterRepository;
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
            fillUserParameters(user);
            authorizationService.authorizeUser(user);
        }
        return true;
    }

    private void fillUserParameters(User user) {
        UserParameter parameter1 = new UserParameter();
        parameter1.setTitle("Manipulation");
        parameter1.setValue(0);
        parameter1.setUser(user);
        userParameterRepository.save(parameter1);

        UserParameter parameter2 = new UserParameter();
        parameter2.setTitle("Intellect");
        parameter2.setValue(0);
        parameter2.setUser(user);
        userParameterRepository.save(parameter2);

        UserParameter parameter3 = new UserParameter();
        parameter3.setTitle("Knowledge");
        parameter3.setValue(0);
        parameter3.setUser(user);
        userParameterRepository.save(parameter3);
    }

    @Override
    public PageDto<String> getFriendsByUsername(int page, int pageSize) {
        String username = authorizationService.getProfileOfCurrent().getUsername();
        Page<User> result = userRepository.findFriendsByUsername(username, PagesUtility.createPageableUnsorted(page, pageSize));
        return PageDto.of(result.getTotalElements(), page, mapToDto(result.getContent()));
    }

    @Override
    public List<UserParameterDto> findParameters(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Invalid Credentials"));
        return user.getUserParameters().stream()
                .map(UserParameterMapper.INSTANCE::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public boolean updateParameter(Long parameterId, Integer value) {
        UserParameter userParameter = userParameterRepository.findById(parameterId)
                .orElseThrow(() -> new EntityNotFoundException("User parameter no found"));
        userParameter.setValue(value);
        userParameterRepository.save(userParameter);
        return true;
    }

    private List<String> mapToDto(List<User> users) {
        return users.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

}
