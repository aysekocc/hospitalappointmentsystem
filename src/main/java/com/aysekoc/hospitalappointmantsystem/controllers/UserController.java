package com.aysekoc.hospitalappointmantsystem.controllers;


import com.aysekoc.hospitalappointmantsystem.config.JwtToken;
import com.aysekoc.hospitalappointmantsystem.entities.User;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.UserService;
import com.aysekoc.hospitalappointmantsystem.services.dtos.UserDto.CreateUserRequest;
import com.aysekoc.hospitalappointmantsystem.services.dtos.UserDto.UpdateUser;
import com.aysekoc.hospitalappointmantsystem.services.dtos.UserDto.UserLoginRequest;
import com.aysekoc.hospitalappointmantsystem.services.dtos.Doctor.CreateDoctorDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5198")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtToken jwtToken;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody CreateUserRequest createUserRequest) {
        userService.register(createUserRequest);
        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserLoginRequest request) {
        try {
            Map<String, Object> response = userService.login(request);
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Geçersiz kullanıcı adı veya şifre"));
        }
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/list/id")
    public ResponseEntity<Optional<User>> findById(@RequestParam Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/list/name")
    public ResponseEntity<List<User>> findByName(@RequestParam String name) {
        return ResponseEntity.ok(userService.findByName(name));
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @PutMapping("/updateUser")
    public ResponseEntity<Void> updateUser(@RequestBody User user) {
        userService.updateUser(user);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('ROLE_DOCTOR','ROLE_USER')")
    @GetMapping("/userAll")
    public ResponseEntity<List<User>> findUserAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @DeleteMapping("/deleteUser/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
