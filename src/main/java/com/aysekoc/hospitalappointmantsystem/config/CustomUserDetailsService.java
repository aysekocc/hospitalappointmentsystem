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
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            return new org.springframework.security.core.userdetails.User(
                    user.get().getUsername(),
                    user.get().getPassword(),
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_" + user.get().getRole().name()))
            );
        }

        Optional<Doctor> doctor = doctorRepository.findByUsername(username);
        if (doctor.isPresent()) {
            return new org.springframework.security.core.userdetails.User(
                    doctor.get().getUsername(),
                    doctor.get().getPassword(),
                    Collections.singleton(new SimpleGrantedAuthority("ROLE_" + doctor.get().getRole().name()))
            );
        }

        throw new UsernameNotFoundException("User/Doctor not found: " + username);
    }

}