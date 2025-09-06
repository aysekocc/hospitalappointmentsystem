package com.aysekoc.hospitalappointmantsystem.controllers;


import com.aysekoc.hospitalappointmantsystem.config.JwtToken;
import com.aysekoc.hospitalappointmantsystem.entities.User;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.UserService;
import com.aysekoc.hospitalappointmantsystem.services.dtos.UserDto.CreateUserRequest;
import com.aysekoc.hospitalappointmantsystem.services.dtos.UserDto.UserLoginRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtToken jwtToken;

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','USER')")
    @PostMapping("/register")
    public void register(@RequestBody CreateUserRequest req) {
        userService.register(req);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','USER')")
    @PostMapping("/login")
    public String login(@RequestBody UserLoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        if (authentication.isAuthenticated()) {
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            return jwtToken.generateToken(request.getUsername(), role);
        } else {
            throw new RuntimeException("Invalid login attempt");
        }
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/list/id")
    public Optional<User> findById(@RequestParam UUID id) {
        return userService.findById(id);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/list/name")
    public List<User> findByName(@RequestParam String name) {
        return userService.findByname(name);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @PutMapping("/updateUser")
    public ResponseEntity<Void> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR','USER')")
    @GetMapping("/userAll")
    public ResponseEntity<List<User>> findUserAll() {
        List<User> userAll = userService.findAll();
        return new ResponseEntity<>(userAll, HttpStatus.OK);
    }

    @PreAuthorize("hasAnyRole('ADMIN','DOCTOR')")
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable UUID id) {
        userService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
