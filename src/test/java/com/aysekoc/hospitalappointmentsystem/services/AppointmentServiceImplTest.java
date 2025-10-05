package com.aysekoc.hospitalappointmentsystem.services;


import com.aysekoc.hospitalappointmantsystem.entities.Appointment;
import com.aysekoc.hospitalappointmantsystem.entities.User;
import com.aysekoc.hospitalappointmantsystem.mapper.AppointmentMapper;
import com.aysekoc.hospitalappointmantsystem.repositories.AppointmentRepository;
import com.aysekoc.hospitalappointmantsystem.services.concretes.AppointmentServiceImpl;
import com.aysekoc.hospitalappointmantsystem.services.concretes.UserServiceImpl;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.CreateAppointment;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.AppointmentListUserDto;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.AppointmentListDoctorDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

class AppointmentServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private UserServiceImpl userServiceImpl;

    @Mock
    private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAppointment() {
        CreateAppointment createAppointment = new CreateAppointment();
        Appointment mappedAppointment = new Appointment();
        Mockito.when(appointmentMapper.createAppointment(createAppointment)).thenReturn(mappedAppointment);

        String result = appointmentService.createAppointment(createAppointment);

        Mockito.verify(appointmentRepository, Mockito.times(1)).save(mappedAppointment);
        Assertions.assertEquals("Created", result);
    }

    @Test
    void testFindByIdExists() {
        Appointment appointment = new Appointment();
        Mockito.when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        Optional<Appointment> result = appointmentService.findById(1L);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(appointment, result.get());
    }

    @Test
    void testFindByIdNotExists() {
        Mockito.when(appointmentRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Appointment> result = appointmentService.findById(2L);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testFindByUsername_UserExists() {
        User user = new User();
        Mockito.when(userServiceImpl.findByUsername("ayse")).thenReturn(Optional.of(user));

        List<Appointment> appointments = Collections.singletonList(new Appointment());
        Mockito.when(appointmentRepository.findByUser(user)).thenReturn(appointments);

        List<Appointment> result = appointmentService.findByUsername("ayse");

        Assertions.assertEquals(appointments, result);
    }

    @Test
    void testFindByUsername_UserNotFound() {
        Mockito.when(userServiceImpl.findByUsername("ayse")).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            appointmentService.findByUsername("ayse");
        });

        Assertions.assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testUserListReturnsEmpty() {
        Mockito.when(appointmentRepository.findByUserId(1L)).thenReturn(Collections.emptyList());

        List<AppointmentListUserDto> result = appointmentService.userList(1L);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testGetAppointmentsByDoctorIdReturnsEmpty() {
        Mockito.when(appointmentRepository.findByDoctorId(1L)).thenReturn(Collections.emptyList());

        List<AppointmentListDoctorDto> result = appointmentService.getAppointmentsByDoctorId(1L);

        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void testDeleteAppointmentExists() {
        Mockito.when(appointmentRepository.existsById(1L)).thenReturn(true);

        appointmentService.deleteAppointment(1L);

        Mockito.verify(appointmentRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void testDeleteAppointmentNotExists() {
        Mockito.when(appointmentRepository.existsById(2L)).thenReturn(false);

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            appointmentService.deleteAppointment(2L);
        });

        Assertions.assertEquals("Appointment not found with id: 2", exception.getMessage());
    }
}
