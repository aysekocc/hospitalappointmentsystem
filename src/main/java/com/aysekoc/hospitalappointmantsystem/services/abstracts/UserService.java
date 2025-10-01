package com.aysekoc.hospitalappointmantsystem.services.abstracts;

import com.aysekoc.hospitalappointmantsystem.entities.User;
import com.aysekoc.hospitalappointmantsystem.services.dtos.UserDto.CreateUserRequest;
import com.aysekoc.hospitalappointmantsystem.services.dtos.UserDto.UpdateUser;
import com.aysekoc.hospitalappointmantsystem.services.dtos.UserDto.UserLoginRequest;
import com.aysekoc.hospitalappointmantsystem.services.dtos.doctor.CreateDoctorDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {
    void registerUser(CreateUserRequest request);
    void registerDoctor(CreateDoctorDto request);
    User findById(Long id);
    List<User> findByName(String name);
    List<User> findAll();
    void updateUser(User user);
    Optional<User> findByUsername(String username);

    void deleteById(Long id);
    Map<String, Object> login(UserLoginRequest req);

}
