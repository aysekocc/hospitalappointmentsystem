package com.aysekoc.hospitalappointmantsystem.controllers;


import com.aysekoc.hospitalappointmantsystem.entities.Doctor;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctor")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @PostMapping("/create")
    public void saveDoctor(@RequestBody Doctor doctor) {
        doctorService.save(doctor);
    }

    @GetMapping("/list/name")
    public List<Doctor> findByName(String name) {
        return doctorService.findByName(name);
    }

    @GetMapping("/list/title")
    public List<Doctor> findByTitle(String title) {
        return doctorService.findByTitle(title);
    }

    @GetMapping("/list/gender")
    public List<Doctor> findByGender(boolean gender) {
        return doctorService.findByGender(gender);
    }

    @GetMapping("/list")
    public List<Doctor> findAll() {
        return doctorService.findAll();
    }

}
