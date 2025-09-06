package com.aysekoc.hospitalappointmantsystem.services.concretes;

import com.aysekoc.hospitalappointmantsystem.entities.Doctor;
import com.aysekoc.hospitalappointmantsystem.repositories.DoctorRepository;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.DoctorService;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;

    @Override
    public void save(Doctor doctor) {
        doctor.setId(doctor.getId());
        doctor.setName(doctor.getName());
        doctor.setSurname(doctor.getSurname());
        doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> findByName(String name) {
        List<Doctor> doctor = doctorRepository.findByName(name);
        if(doctor.isEmpty()){
            throw new NullPointerException("Doctor Not Found");
        }
        return doctor;
    }


    @Override
    public List<Doctor> findByTitle(String title) {
        List<Doctor> doctor = doctorRepository.findByTitle(title);
        return doctor;
    }

    @Override
    public List<Doctor> findByGender(boolean gender) {
        List<Doctor> doctor = doctorRepository.findByGender(gender);
        return doctor;
    }

    @Override
    public List<Doctor> findAll() {
        List<Doctor> doctor = doctorRepository.findAll();
        return doctor;
    }
}
