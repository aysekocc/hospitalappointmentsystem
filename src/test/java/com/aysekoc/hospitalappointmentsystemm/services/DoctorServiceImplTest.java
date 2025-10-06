package com.aysekoc.hospitalappointmentsystemm.services;

import com.aysekoc.hospitalappointmantsystem.entities.Doctor;
import com.aysekoc.hospitalappointmantsystem.entities.Specialty;
import com.aysekoc.hospitalappointmantsystem.mapper.DoctorMapper;
import com.aysekoc.hospitalappointmantsystem.repositories.DoctorRepository;
import com.aysekoc.hospitalappointmantsystem.services.concretes.DoctorServiceImpl;
import com.aysekoc.hospitalappointmantsystem.services.dtos.DoctorDto.CreateDoctorDto;
import com.aysekoc.hospitalappointmantsystem.services.dtos.DoctorDto.DoctorListDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private DoctorMapper doctorMapper;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private Doctor doctor;
    private DoctorListDto doctorListDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        doctor = new Doctor();
        doctor.setId(1L);
        doctor.setName("John");
        doctor.setSurname("Doe");
        doctor.setSpecialty(Specialty.KARDIYOLOJI);
        doctor.setTitle("MD");
        doctor.setGender(true);

        doctorListDto = new DoctorListDto();
        doctorListDto.setName("John");
        doctorListDto.setSurname("Doe");
        doctorListDto.setSpecialty(Specialty.KARDIYOLOJI);
        doctorListDto.setTitle("MD");
    }

    @Test
    void testSave() {
        CreateDoctorDto createDoctorDto = new CreateDoctorDto();
        when(doctorMapper.createDoctorMap(createDoctorDto)).thenReturn(doctor);

        doctorService.save(createDoctorDto);

        verify(doctorRepository, times(1)).save(doctor);
    }

    @Test
    void testFindByNameFound() {
        when(doctorRepository.findByName("John")).thenReturn(List.of(doctor));
        when(doctorMapper.listDoctorDto(doctor)).thenReturn(doctorListDto);

        List<DoctorListDto> result = doctorService.findByName("John");

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
    }

    @Test
    void testFindByNameNotFound() {
        when(doctorRepository.findByName("Jane")).thenReturn(List.of());

        assertThrows(NullPointerException.class, () -> doctorService.findByName("Jane"));
    }

    @Test
    void testFindByTitle() {
        when(doctorRepository.findByTitle("MD")).thenReturn(List.of(doctor));
        when(doctorMapper.listDoctorDto(doctor)).thenReturn(doctorListDto);

        List<DoctorListDto> result = doctorService.findByTitle("MD");

        assertEquals(1, result.size());
    }

    @Test
    void testFindByGender() {
        when(doctorRepository.findByGender(true)).thenReturn(List.of(doctor));
        when(doctorMapper.listDoctorDto(doctor)).thenReturn(doctorListDto);

        List<DoctorListDto> result = doctorService.findByGender(true);

        assertEquals(1, result.size());
    }

    @Test
    void testFindAll() {
        Page<Doctor> page = new PageImpl<>(List.of(doctor));
        when(doctorRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);
        when(doctorMapper.listDoctorDto(doctor)).thenReturn(doctorListDto);

        Page<DoctorListDto> result = doctorService.findAll(10, 0);

        assertEquals(1, result.getContent().size());
        assertEquals("John", result.getContent().get(0).getName());
    }

    @Test
    void testFindById() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));

        Optional<Doctor> result = doctorService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("John", result.get().getName());
    }

    @Test
    void testGetAllDoctors() {
        when(doctorRepository.findAll()).thenReturn(List.of(doctor));
        when(doctorMapper.listDoctorDto(doctor)).thenReturn(doctorListDto);

        List<DoctorListDto> result = doctorService.getAllDoctors();

        assertEquals(1, result.size());
    }

    @Test
    void testFindFirstBySpecialty() {
        when(doctorRepository.findFirstBySpecialty(Specialty.KARDIYOLOJI)).thenReturn(Optional.of(doctor));

        Optional<Doctor> result = doctorService.findFirstBySpecialty(Specialty.KARDIYOLOJI);

        assertTrue(result.isPresent());
    }
}
