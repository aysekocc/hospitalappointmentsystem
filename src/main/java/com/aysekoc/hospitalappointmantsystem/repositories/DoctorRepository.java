package com.aysekoc.hospitalappointmantsystem.repositories;

import com.aysekoc.hospitalappointmantsystem.entities.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findByHospital_Name(String hospitalName);
    List<Doctor> findByName(String name);
    List<Doctor> findBySpecialty(String specialty);
    List<Doctor> findByTitle(String title);
    List<Doctor> findByGender(boolean gender);  // true/false
    List<Doctor> findByHospitalId(Long id);



}
