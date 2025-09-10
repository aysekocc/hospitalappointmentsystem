package com.aysekoc.hospitalappointmantsystem.controllers;


import com.aysekoc.hospitalappointmantsystem.services.abstracts.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctor")
@CrossOrigin(origins = "http://localhost:5198")
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
    public List<DoctorListDto> findByHospitalName(String name) {
        return doctorService.findByName(name);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DOCTOR','ROLE_USER')")
    @GetMapping("/list/title")
    public List<DoctorListDto> findByTitle(String title) {
        return doctorService.findByTitle(title);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DOCTOR','ROLE_USER')")
    @GetMapping("/list/gender")
    public List<DoctorListDto> findByGender(boolean gender) {
        return doctorService.findByGender(gender);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DOCTOR','ROLE_USER')")
    @GetMapping("/list")
    public Page<DoctorListDto> findAll(@RequestParam(defaultValue = "5") int size,
                                       @RequestParam(defaultValue = "0") int page) {
        return doctorService.findAll(size, page);
    }

}
