package com.aysekoc.hospitalappointmantsystem.controllers;


import com.aysekoc.hospitalappointmantsystem.entities.Doctor;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctor")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DOCTOR')")
    @PostMapping("/create")
    public void createByHospital(@RequestBody Doctor doctor) {
        doctorService.save(doctor);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DOCTOR','ROLE_USER')")
    @GetMapping("/list/name")
    public List<Doctor> findByHospitalName(String name) {
        return doctorService.findByName(name);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DOCTOR','ROLE_USER')")
    @GetMapping("/list/title")
    public List<Doctor> findByTitle(String title) {
        return doctorService.findByTitle(title);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DOCTOR','ROLE_USER')")
    @GetMapping("/list/gender")
    public List<Doctor> findByGender(boolean gender) {
        return doctorService.findByGender(gender);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DOCTOR','ROLE_USER')")
    @GetMapping("/list")
    public List<Doctor> findAll() {
        return doctorService.findAll();
    }

}
