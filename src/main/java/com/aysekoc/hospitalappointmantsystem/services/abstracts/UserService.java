package com.aysekoc.hospitalappointmantsystem.services.abstracts;

import com.aysekoc.hospitalappointmantsystem.entities.User;
import com.aysekoc.hospitalappointmantsystem.services.dtos.UserDto.CreateUserRequest;
import com.aysekoc.hospitalappointmantsystem.services.dtos.UserDto.LoginResponse;
import com.aysekoc.hospitalappointmantsystem.services.dtos.UserDto.UserLoginRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    void register(CreateUserRequest  req);
    String login(UserLoginRequest request);
    User findById(Long id);
    List<User> findByName(String name);
    List<User> findAll();
    void updateUser(User user);
    Optional<User> findByUsername(String username);

    void deleteById(Long id);

}
