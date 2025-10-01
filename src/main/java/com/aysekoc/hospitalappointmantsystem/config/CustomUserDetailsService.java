package com.aysekoc.hospitalappointmantsystem.config;

import com.aysekoc.hospitalappointmantsystem.entities.Doctor;
import com.aysekoc.hospitalappointmantsystem.entities.User;
import com.aysekoc.hospitalappointmantsystem.repositories.DoctorRepository;
import com.aysekoc.hospitalappointmantsystem.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final DoctorRepository doctorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(user -> new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())) // Enum -> ROLE_X
                ))
                .orElseGet(() -> doctorRepository.findByUsername(username)
                        .map(doctor -> new org.springframework.security.core.userdetails.User(
                                doctor.getUsername(),
                                doctor.getPassword(),
                                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + doctor.getRole().name())) // Enum -> ROLE_X
                        ))
                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username))
                );
    }
}