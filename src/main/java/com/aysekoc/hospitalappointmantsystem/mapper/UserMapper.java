package com.aysekoc.hospitalappointmantsystem.mapper;

import com.aysekoc.hospitalappointmantsystem.entities.Roles;
import com.aysekoc.hospitalappointmantsystem.entities.User;
import com.aysekoc.hospitalappointmantsystem.services.dtos.UserDto.CreateUserRequest;
import com.aysekoc.hospitalappointmantsystem.services.dtos.UserDto.UpdateUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;



@Component
@RequiredArgsConstructor
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public User registerUser(CreateUserRequest req) {

        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setName(req.getName());
        user.setSurname(req.getSurname());
        user.setIdentity(req.getIdentity());
        user.setGender(req.getGender());
        user.setAge(req.getAge());
        user.setRole(Roles.valueOf("ROLE_" + req.getRole()));
        return user;
    }
    public User updateUser(UpdateUser dto) {

        User userEntity = new User();
        userEntity.setId(dto.getId());
        userEntity.setUsername(dto.getUsername());
        userEntity.setSurname(dto.getSurname());
        userEntity.setGender(dto.getGender());
        userEntity.setAge(dto.getAge());
        userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        userEntity.setName(dto.getName());
        userEntity.setIdentity(dto.getIdentity());
        return  userEntity;

}
}