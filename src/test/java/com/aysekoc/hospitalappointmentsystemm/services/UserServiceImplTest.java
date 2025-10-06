package com.aysekoc.hospitalappointmentsystemm.services;

import com.aysekoc.hospitalappointmantsystem.config.JwtToken;
import com.aysekoc.hospitalappointmantsystem.entities.*;
import com.aysekoc.hospitalappointmantsystem.mapper.UserMapper;
import com.aysekoc.hospitalappointmantsystem.repositories.DoctorRepository;
import com.aysekoc.hospitalappointmantsystem.repositories.HospitalRepository;
import com.aysekoc.hospitalappointmantsystem.repositories.UserRepository;
import com.aysekoc.hospitalappointmantsystem.services.concretes.UserServiceImpl;
import com.aysekoc.hospitalappointmantsystem.services.dtos.DoctorDto.CreateDoctorDto;
import com.aysekoc.hospitalappointmantsystem.services.dtos.UserDto.CreateUserRequest;
import com.aysekoc.hospitalappointmantsystem.services.dtos.UserDto.UserLoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtToken jwtToken;

    @Mock
    private UserMapper userMapper;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private HospitalRepository hospitalRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private Doctor doctor;
    private Hospital hospital;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setPassword("encodedpass");
        user.setRole(Roles.ROLE_USER);

        doctor = new Doctor();
        doctor.setId(2L);
        doctor.setUsername("testdoctor");
        doctor.setPassword("encodedpass");
        doctor.setRole(Roles.ROLE_DOCTOR);

        hospital = new Hospital();
        hospital.setId(1L);
    }

    @Test
    void testRegisterUser() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("testuser");
        request.setPassword("pass");
        request.setName("Test");
        request.setSurname("User");
        request.setAge(30);

        when(passwordEncoder.encode("pass")).thenReturn("encodedpass");

        userService.registerUser(request);

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterDoctor() {
        CreateDoctorDto request = new CreateDoctorDto();
        request.setUsername("testdoctor");
        request.setPassword("pass");
        request.setName("Dr");
        request.setSurname("Who");
        request.setAge(40);
        request.setHospitalId(1L);
        request.setGender(true);
        request.setSpecialty(Specialty.KARDIYOLOJI);
        request.setTitle("MD");

        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(hospital));
        when(passwordEncoder.encode("pass")).thenReturn("encodedpass");

        userService.registerDoctor(request);

        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void testLoginUserSuccess() {
        UserLoginRequest request = new UserLoginRequest();
        request.setUsername("testuser");
        request.setPassword("pass");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass", "encodedpass")).thenReturn(true);
        when(jwtToken.generateToken("testuser", "ROLE_USER")).thenReturn("token123");

        var result = userService.login(request);

        assertEquals("token123", result.get("token"));
        assertEquals("ROLE_USER", result.get("role"));
        assertEquals(1L, result.get("userId"));
    }

    @Test
    void testLoginDoctorSuccess() {
        UserLoginRequest request = new UserLoginRequest();
        request.setUsername("testdoctor");
        request.setPassword("pass");

        when(userRepository.findByUsername("testdoctor")).thenReturn(Optional.empty());
        when(doctorRepository.findByUsername("testdoctor")).thenReturn(Optional.of(doctor));
        when(passwordEncoder.matches("pass", "encodedpass")).thenReturn(true);
        when(jwtToken.generateToken("testdoctor", "ROLE_DOCTOR")).thenReturn("token456");

        var result = userService.login(request);

        assertEquals("token456", result.get("token"));
        assertEquals("ROLE_DOCTOR", result.get("role"));
        assertEquals(2L, result.get("userId"));
    }

    @Test
    void testLoginFailure() {
        UserLoginRequest request = new UserLoginRequest();
        request.setUsername("unknown");
        request.setPassword("pass");

        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());
        when(doctorRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> userService.login(request));
    }

    @Test
    void testFindById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> result = userService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("testuser", result.get().getUsername());
    }

    @Test
    void testDeleteByIdSuccess() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> userService.deleteById(1L));
    }
}
