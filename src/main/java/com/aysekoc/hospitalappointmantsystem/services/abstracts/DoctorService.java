package com.aysekoc.hospitalappointmantsystem.services.abstracts;

import com.aysekoc.hospitalappointmantsystem.entities.Doctor;
import com.aysekoc.hospitalappointmantsystem.entities.Specialty;
import com.aysekoc.hospitalappointmantsystem.services.dtos.DoctorDto.CreateDoctorDto;
import com.aysekoc.hospitalappointmantsystem.services.dtos.DoctorDto.DoctorListDto;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    void save(Doctor doctor);
    List<Doctor> findByName(String name);
    List<Doctor> findByTitle(String title);
    List<Doctor> findByGender(boolean gender);
    List<Doctor> findAll();
    Optional<Doctor> findById(Long id);
    Optional<Doctor> findByUsername(String username);
    List<DoctorListDto> getAllDoctors();
    Optional<Doctor> findFirstBySpecialty(Specialty specialty);


}
