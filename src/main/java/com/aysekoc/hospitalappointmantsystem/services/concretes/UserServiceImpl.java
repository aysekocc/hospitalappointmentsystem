package com.aysekoc.hospitalappointmantsystem.services.concretes;

import com.aysekoc.hospitalappointmantsystem.config.JwtToken;
import com.aysekoc.hospitalappointmantsystem.entities.User;
import com.aysekoc.hospitalappointmantsystem.repositories.UserRepository;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.UserService;
import com.aysekoc.hospitalappointmantsystem.services.dtos.UserDto.CreateUserRequest;
import com.aysekoc.hospitalappointmantsystem.services.dtos.UserDto.UserLoginRequest;
import com.aysekoc.hospitalappointmantsystem.services.dtos.doctor.CreateDoctorDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    public void registerDoctor(CreateDoctorDto request) {
        Doctor doctor = new Doctor();
        doctor.setName(request.getName());
        doctor.setSurname(request.getSurname());
        doctor.setPassword(passwordEncoder.encode(request.getPassword()));
        doctor.setUsername(request.getUsername());
        doctor.setSpecialty(request.getSpecialty());
        doctor.setTitle(request.getTitle());
        doctor.setGender(request.isGender());
        doctor.setAge(request.getAge());
        Hospital hospital = hospitalRepository.findById(request.getHospitalId())
                .orElseThrow(() -> new RuntimeException("Hospital not found"));
        doctor.setHospital(hospital);
        doctor.setRole(Roles.ROLE_DOCTOR);
        doctorRepository.save(doctor);
    }

    @Override
    public Map<String, Object> login(UserLoginRequest req) {
        Map<String, Object> res = new HashMap<>();

        // 1. User repository kontrol et
        Optional<User> user = userRepository.findByUsername(req.getUsername());
        if(user.isPresent() && passwordEncoder.matches(req.getPassword(), user.get().getPassword())) {
            String token = jwtToken.generateToken(user.get().getUsername(), user.get().getRole().name());
            res.put("token", token);
            res.put("role", user.get().getRole().name());
            res.put("message", "Login successful");
            res.put("userId", user.get().getId());
            return res;
        }

        // 2. Doctor repository kontrol et
        Optional<Doctor> doctor = doctorRepository.findByUsername(req.getUsername());
        if(doctor.isPresent() && passwordEncoder.matches(req.getPassword(), doctor.get().getPassword())) {
            String token = jwtToken.generateToken(doctor.get().getUsername(), doctor.get().getRole().name());
            res.put("token", token);
            res.put("role", doctor.get().getRole().name());
            res.put("message", "Login successful");
            res.put("userId", doctor.get().getId());
            return res;
        }

        throw new BadCredentialsException("Invalid username or password");
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
