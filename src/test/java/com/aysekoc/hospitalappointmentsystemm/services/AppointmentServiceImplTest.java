package com.aysekoc.hospitalappointmentsystemm.services;

import com.aysekoc.hospitalappointmantsystem.entities.Appointment;
import com.aysekoc.hospitalappointmantsystem.entities.User;
import com.aysekoc.hospitalappointmantsystem.exception.AppointmentConflictException;
import com.aysekoc.hospitalappointmantsystem.mapper.AppointmentMapper;
import com.aysekoc.hospitalappointmantsystem.repositories.AppointmentRepository;
import com.aysekoc.hospitalappointmantsystem.services.concretes.AppointmentServiceImpl;
import com.aysekoc.hospitalappointmantsystem.services.concretes.UserServiceImpl;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.AppointmentListDoctorDto;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.AppointmentListUserDto;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.CreateAppointment;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.Mockito.*;

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

    // ✅ createAppointment - normal durum
    @Test
    void testCreateAppointment_Success() {
        CreateAppointment createAppointment = new CreateAppointment();
        createAppointment.setStartedDate(LocalDateTime.now());
        createAppointment.setEndedDate(LocalDateTime.now().plusMinutes(30));
        createAppointment.setDoctor(1L);

        when(appointmentRepository.existsConflict(1L, createAppointment.getStartedDate(), createAppointment.getEndedDate()))
                .thenReturn(false);

        Appointment mappedAppointment = new Appointment();
        when(appointmentMapper.createAppointment(createAppointment)).thenReturn(mappedAppointment);

        String result = appointmentService.createAppointment(createAppointment);

        verify(appointmentRepository, times(1)).save(mappedAppointment);
        Assertions.assertEquals("Created", result);
    }

    // ✅ createAppointment - çakışma durumu
    @Test
    void testCreateAppointment_Conflict() {
        CreateAppointment createAppointment = new CreateAppointment();
        createAppointment.setStartedDate(LocalDateTime.now());
        createAppointment.setEndedDate(LocalDateTime.now().plusMinutes(30));
        createAppointment.setDoctor(2L);

        when(appointmentRepository.existsConflict(2L, createAppointment.getStartedDate(), createAppointment.getEndedDate()))
                .thenReturn(true);

        Assertions.assertThrows(AppointmentConflictException.class, () -> {
            appointmentService.createAppointment(createAppointment);
        });

        verify(appointmentRepository, never()).save(any());
    }

    // ✅ findById
    @Test
    void testFindByIdExists() {
        Appointment appointment = new Appointment();
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));

        Optional<Appointment> result = appointmentService.findById(1L);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(appointment, result.get());
    }

    @Test
    void testFindByIdNotExists() {
        when(appointmentRepository.findById(2L)).thenReturn(Optional.empty());

        Optional<Appointment> result = appointmentService.findById(2L);

        Assertions.assertTrue(result.isEmpty());
    }

    // ✅ findByUsername
    @Test
    void testFindByUsername_UserExists() {
        User user = new User();
        when(userServiceImpl.findByUsername("ayse")).thenReturn(Optional.of(user));

        List<Appointment> appointments = Collections.singletonList(new Appointment());
        when(appointmentRepository.findByUser(user)).thenReturn(appointments);

        List<Appointment> result = appointmentService.findByUsername("ayse");

        Assertions.assertEquals(appointments, result);
    }

    @Test
    void testFindByUsername_UserNotFound() {
        when(userServiceImpl.findByUsername("ayse")).thenReturn(Optional.empty());

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            appointmentService.findByUsername("ayse");
        });

        Assertions.assertEquals("User not found", exception.getMessage());
    }

    // ✅ userList
    @Test
    void testUserListReturnsEmpty() {
        when(appointmentRepository.findByUserId(1L)).thenReturn(Collections.emptyList());
        List<AppointmentListUserDto> result = appointmentService.userList(1L);
        Assertions.assertTrue(result.isEmpty());
    }

    // ✅ getAppointmentsByDoctorId
    @Test
    void testGetAppointmentsByDoctorIdReturnsEmpty() {
        when(appointmentRepository.findByDoctorId(1L)).thenReturn(Collections.emptyList());
        List<AppointmentListDoctorDto> result = appointmentService.getAppointmentsByDoctorId(1L);
        Assertions.assertTrue(result.isEmpty());
    }

    // ✅ deleteAppointment
    @Test
    void testDeleteAppointmentExists() {
        when(appointmentRepository.existsById(1L)).thenReturn(true);
        appointmentService.deleteAppointment(1L);
        verify(appointmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteAppointmentNotExists() {
        when(appointmentRepository.existsById(2L)).thenReturn(false);

        Exception exception = Assertions.assertThrows(RuntimeException.class, () -> {
            appointmentService.deleteAppointment(2L);
        });

        Assertions.assertEquals("Appointment not found with id: 2", exception.getMessage());
    }

    // ✅ getAppointments (sayfalama)
    @Test
    void testGetAppointments() {
        Page<Appointment> page = new PageImpl<>(List.of(new Appointment()));
        when(appointmentRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Appointment> result = appointmentService.getAppointments(0, 5);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getContent().size());
        verify(appointmentRepository, times(1)).findAll(any(Pageable.class));
    }

    // ✅ findBookedSlots
    @Test
    void testFindBookedSlots() {
        LocalDate date = LocalDate.now();
        List<LocalDateTime> expectedSlots = List.of(LocalDateTime.now(), LocalDateTime.now().plusHours(1));

        when(appointmentRepository.findByDoctorAndDateRange(eq(5L), any(), any())).thenReturn(expectedSlots);

        List<LocalDateTime> result = appointmentService.findBookedSlots(5L, date);

        Assertions.assertEquals(expectedSlots, result);
        verify(appointmentRepository, times(1))
                .findByDoctorAndDateRange(eq(5L), any(LocalDateTime.class), any(LocalDateTime.class));
    }
}
