package com.aysekoc.hospitalappointmentsystem.services;

import com.aysekoc.hospitalappointmantsystem.entities.Appointment;
import com.aysekoc.hospitalappointmantsystem.entities.Prescription;
import com.aysekoc.hospitalappointmantsystem.entities.User;
import com.aysekoc.hospitalappointmantsystem.mapper.PrescriptionMapper;
import com.aysekoc.hospitalappointmantsystem.repositories.PrescriptionRepository;
import com.aysekoc.hospitalappointmantsystem.services.concretes.PrescriptionServiceImpl;
import com.aysekoc.hospitalappointmantsystem.services.dtos.PrescriptionDto.CreatePrescription;
import com.aysekoc.hospitalappointmantsystem.services.dtos.PrescriptionDto.PrescriptionListDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PrescriptionServiceImplTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;

    @Mock
    private PrescriptionMapper prescriptionMapper;

    @InjectMocks
    private PrescriptionServiceImpl prescriptionService;

    private Prescription prescription;
    private PrescriptionListDto prescriptionListDto;
    private User user;
    private Appointment appointment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        appointment = new Appointment();
        appointment.setId(1L);

        prescription = new Prescription();
        prescription.setId(1L);
        prescription.setMedicineName("Aspirin");
        prescription.setDiagnosis("Headache");
        prescription.setDate(LocalDateTime.now());
        prescription.setHashPrescription("HASH123");
        prescription.setUser(user);
        prescription.setAppointment(appointment);

        prescriptionListDto = new PrescriptionListDto();
        prescriptionListDto.setId(1L);
        prescriptionListDto.setMedicineName("Aspirin");
        prescriptionListDto.setDiagnosis("Headache");
        prescriptionListDto.setDate(prescription.getDate());
        prescriptionListDto.setHashPrescription("HASH123");
        prescriptionListDto.setUserId(user.getId());
    }

    @Test
    void testCreate() {
        CreatePrescription createPrescription = new CreatePrescription();
        createPrescription.setMedicineName("Aspirin");
        createPrescription.setDiagnosis("Headache");
        createPrescription.setUserId(1L);
        createPrescription.setAppointmentId(1L);

        when(prescriptionMapper.createMap(createPrescription)).thenReturn(prescription);

        prescriptionService.create(createPrescription);

        verify(prescriptionRepository, times(1)).save(prescription);
    }

    @Test
    void testFindByDateFound() {
        when(prescriptionRepository.findByDate(prescription.getDate())).thenReturn(List.of(prescription));

        List<Prescription> result = prescriptionService.findByDate(prescription.getDate());

        assertEquals(1, result.size());
        assertEquals("Aspirin", result.get(0).getMedicineName());
    }

    @Test
    void testFindByDateNotFound() {
        when(prescriptionRepository.findByDate(LocalDateTime.now())).thenReturn(List.of());

        assertThrows(NullPointerException.class, () -> prescriptionService.findByDate(LocalDateTime.now()));
    }

    @Test
    void testDeleteById() {
        when(prescriptionRepository.findById(1L)).thenReturn(Optional.of(prescription));

        prescriptionService.deleteById(1L);

        verify(prescriptionRepository, times(1)).delete(prescription);
    }

    @Test
    void testFindByHashPrescription() {
        when(prescriptionRepository.findByHashPrescription("HASH123")).thenReturn(Optional.of(prescription));

        Optional<Prescription> result = prescriptionService.findByHashPrescription("HASH123");

        assertTrue(result.isPresent());
        assertEquals("HASH123", result.get().getHashPrescription());
    }

    @Test
    void testFindById() {
        Page<Prescription> page = new PageImpl<>(List.of(prescription));
        PageRequest pageable = PageRequest.of(0, 10);

        when(prescriptionRepository.findByUserId(1L, pageable)).thenReturn(page);
        when(prescriptionMapper.toDto(any(Prescription.class))).thenReturn(prescriptionListDto);

        Page<PrescriptionListDto> result = prescriptionService.findById(1L, pageable);

        assertEquals(1, result.getContent().size());
        assertEquals("Aspirin", result.getContent().get(0).getMedicineName());
        assertEquals("HASH123", result.getContent().get(0).getHashPrescription());
    }

    @Test
    void testFindByAppointmentId() {
        when(prescriptionRepository.findByAppointmentId(1L)).thenReturn(Optional.of(prescription));
        when(prescriptionMapper.toDto(any(Prescription.class))).thenReturn(prescriptionListDto);

        Optional<PrescriptionListDto> result = prescriptionService.findByAppointmentId(1L);

        assertTrue(result.isPresent());
        assertEquals("HASH123", result.get().getHashPrescription());
    }
}
