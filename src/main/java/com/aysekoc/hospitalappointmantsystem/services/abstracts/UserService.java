package com.aysekoc.hospitalappointmantsystem.services.abstracts;

import com.aysekoc.hospitalappointmantsystem.entities.User;
import com.aysekoc.hospitalappointmantsystem.services.dtos.UserDto.CreateUserRequest;
import com.aysekoc.hospitalappointmantsystem.services.dtos.UserDto.UserLoginRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    void register(CreateUserRequest  req);
    String login(UserLoginRequest request);
    Optional<User> findById(UUID id);
    List<User> findByname(String name);
    List<User> findAll();
    void updateUser(User user);
    void deleteById(UUID id);

}
