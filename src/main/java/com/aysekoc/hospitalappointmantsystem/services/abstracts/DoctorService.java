package com.aysekoc.hospitalappointmantsystem.services.abstracts;

import com.aysekoc.hospitalappointmantsystem.entities.Doctor;

import java.util.List;

public interface DoctorService {
    void save(Doctor doctor);
    List<Doctor> findByName(String name);
    List<Doctor> findByTitle(String title);
    List<Doctor> findByGender(boolean gender);
    List<Doctor> findAll();


}
