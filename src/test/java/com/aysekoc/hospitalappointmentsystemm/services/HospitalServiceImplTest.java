package com.aysekoc.hospitalappointmentsystemm.services;


import com.aysekoc.hospitalappointmantsystem.entities.Hospital;
import com.aysekoc.hospitalappointmantsystem.mapper.HospitalMapper;
import com.aysekoc.hospitalappointmantsystem.repositories.DoctorRepository;
import com.aysekoc.hospitalappointmantsystem.repositories.HospitalRepository;
import com.aysekoc.hospitalappointmantsystem.services.concretes.HospitalServiceImpl;
import com.aysekoc.hospitalappointmantsystem.services.dtos.HospitalDto.CreateHospital;
import com.aysekoc.hospitalappointmantsystem.services.dtos.HospitalDto.HospitalListDto;
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

public class HospitalServiceImplTest {

    @Mock
    private HospitalRepository hospitalRepository;

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private HospitalMapper hospitalMapper;

    @InjectMocks
    private HospitalServiceImpl hospitalService;

    private Hospital hospital;
    private HospitalListDto hospitalListDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        hospital = new Hospital();
        hospital.setId(1L);
        hospital.setName("City Hospital");
        hospital.setTown("Town A");
        hospital.setTownship("Township X");

        hospitalListDto = new HospitalListDto();
        hospitalListDto.setId(1L);
        hospitalListDto.setName("City Hospital");
        hospitalListDto.setTown("Town A");
        hospitalListDto.setTownship("Township X");
    }

    @Test
    void testGetHospitals() {
        Page<Hospital> page = new PageImpl<>(List.of(hospital));
        when(hospitalRepository.findAll(PageRequest.of(0, 10))).thenReturn(page);

        Page<Hospital> result = hospitalService.getHospitals(0, 10);

        assertEquals(1, result.getContent().size());
        assertEquals("City Hospital", result.getContent().get(0).getName());
    }

    @Test
    void testCreateByHospital() {
        CreateHospital createHospital = new CreateHospital();
        createHospital.setName("New Hospital");
        createHospital.setTown("Town B");
        createHospital.setTownship("Township Y");

        hospitalService.createByHospital(createHospital);

        verify(hospitalRepository, times(1)).save(any(Hospital.class));
    }

    @Test
    void testFindByHospitalNameFound() {
        CreateHospital createHospital = new CreateHospital();
        createHospital.setName("City Hospital");

        when(hospitalRepository.findByName(createHospital)).thenReturn(List.of(hospital));

        List<Hospital> result = hospitalService.findByHospitalName(createHospital);

        assertEquals(1, result.size());
        assertEquals("City Hospital", result.get(0).getName());
    }

    @Test
    void testFindByHospitalNameNotFound() {
        CreateHospital createHospital = new CreateHospital();
        createHospital.setName("Unknown Hospital");

        when(hospitalRepository.findByName(createHospital)).thenReturn(List.of());

        assertThrows(NullPointerException.class, () -> hospitalService.findByHospitalName(createHospital));
    }

    @Test
    void testUpdateHospital() {
        CreateHospital createHospital = new CreateHospital();
        createHospital.setName("Updated Hospital");
        createHospital.setTown("Updated Town");
        createHospital.setTownship("Updated Township");

        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(hospital));

        hospitalService.updateHospital(1L, createHospital);

        verify(hospitalRepository, times(1)).save(hospital);
        assertEquals("Updated Hospital", hospital.getName());
    }

    @Test
    void testDeleteByHospital() {
        hospitalService.deleteByHospital(1L);

        verify(hospitalRepository, times(1)).deleteById(1L);
    }

    @Test
    void testFindById() {
        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(hospital));
        when(hospitalMapper.toDto(hospital)).thenReturn(hospitalListDto);

        HospitalListDto result = hospitalService.findById(1L);

        assertEquals("City Hospital", result.getName());
    }

    @Test
    void testFindByIdMap() {
        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(hospital));

        Hospital result = hospitalService.findByIdMap(1L);

        assertEquals("City Hospital", result.getName());
    }

    @Test
    void testGetAllHospitals() {
        when(hospitalRepository.findAll()).thenReturn(List.of(hospital));

        List<Hospital> result = hospitalService.getAllHospitals();

        assertEquals(1, result.size());
    }

    @Test
    void testAddDoctor() {
        // Mocking hospital and doctor
        Long hospitalId = 1L;
        Long doctorId = 2L;
        when(hospitalRepository.findById(hospitalId)).thenReturn(Optional.of(hospital));
        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(mock(com.aysekoc.hospitalappointmantsystem.entities.Doctor.class)));

        hospitalService.addDoctor(doctorId, hospitalId);

        verify(hospitalRepository, times(1)).save(hospital);
    }
}

