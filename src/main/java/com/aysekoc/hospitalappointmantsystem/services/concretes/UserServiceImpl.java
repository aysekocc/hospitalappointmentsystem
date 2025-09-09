package com.aysekoc.hospitalappointmantsystem.services.concretes;

import com.aysekoc.hospitalappointmantsystem.config.JwtToken;
import com.aysekoc.hospitalappointmantsystem.entities.Roles;
import com.aysekoc.hospitalappointmantsystem.entities.User;
import com.aysekoc.hospitalappointmantsystem.repositories.UserRepository;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.UserService;
import com.aysekoc.hospitalappointmantsystem.services.dtos.UserDto.CreateUserRequest;
import com.aysekoc.hospitalappointmantsystem.services.dtos.UserDto.UserLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtToken jwtToken;

    @Override
    public void register(CreateUserRequest req) {
        if (userRepository.findByUsername(req.getUsername()).isPresent()) {
            throw new RuntimeException("User already exists!");
        }
        User user = new User();
        user.setUsername(req.getUsername());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setName(req.getName());
        user.setSurname(req.getSurname());
        user.setIdentity(req.getIdentity());
        user.setGender(req.getGender());
        user.setAge(req.getAge());
        user.setRole(Roles.valueOf("ROLE_" + req.getRole()));

        userRepository.save(user);
    }


    @Override
    public String login(UserLoginRequest req) {
        Optional<User> user = userRepository.findByUsername(req.getUsername());
        if (user.isEmpty() || !passwordEncoder.matches(req.getPassword(), user.get().getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        return jwtToken.generateToken(user.get().getUsername(), user.get().getRole().name());
    }

    @Override
    public Optional<User> findById(Long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NullPointerException("User not found!");
        }
        return user;
    }

    @Override
    public List<User> findByName(String name) {
        List<User> user = userRepository.findByName(name);
        if (user.isEmpty()) {
            throw new NullPointerException("User not found");
        }
        return user;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public void updateUser(User user) {
        Optional<User> optionalUser = userRepository.findById(user.getId());
        if (optionalUser.isEmpty()) {
            throw new NullPointerException("User not found!");
        }
        userRepository.save(user);
    }

    @Override
    public void deleteById(Long id) {
        Optional<User> optionalCustomer = userRepository.findById(id);
        if (optionalCustomer.isEmpty()) {
            throw new NullPointerException();
        }
        userRepository.deleteById(id);
    }
}
