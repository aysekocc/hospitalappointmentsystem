package com.aysekoc.hospitalappointmantsystem.controllers;


import com.aysekoc.hospitalappointmantsystem.services.abstracts.DoctorService;
import com.aysekoc.hospitalappointmantsystem.services.dtos.Doctor.CreateDoctorDto;
import com.aysekoc.hospitalappointmantsystem.services.dtos.Doctor.DoctorListDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.aysekoc.hospitalappointmantsystem.entities.Doctor;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.AppointmentService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctor")
@CrossOrigin(origins = "http://localhost:5198")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @PostMapping("/create")
    public void createByHospital(@RequestBody Doctor doctor) {
        doctorService.save(doctor);
    }

    @PreAuthorize("hasAnyRole('ROLE_DOCTOR','ROLE_USER')")
    @GetMapping("/list/name")
    public List<DoctorListDto> findByHospitalName(@Valid String name) {
        return doctorService.findByName(name);
    }

    @PreAuthorize("hasAnyRole('ROLE_DOCTOR','ROLE_USER')")
    @GetMapping("/list/title")
    public List<DoctorListDto> findByTitle(@Valid String title) {
        return doctorService.findByTitle(title);
    }

    @PreAuthorize("hasAnyRole('ROLE_DOCTOR','ROLE_USER')")
    @GetMapping("/list/gender")
    public List<DoctorListDto> findByGender(@Valid boolean gender) {
        return doctorService.findByGender(gender);
    }

    @PreAuthorize("hasAnyRole('ROLE_DOCTOR','ROLE_USER')")
    @GetMapping("/list")
    public Page<DoctorListDto> findAll(@Valid @RequestParam(defaultValue = "5") int size,
                                       @RequestParam(defaultValue = "0") int page) {
        return doctorService.findAll(size, page);
    }
    @PreAuthorize("hasAnyRole('ROLE_DOCTOR','ROLE_USER')")
    @GetMapping("/all")
    public List<DoctorListDto> getAllDoctors() {
        return doctorService.getAllDoctors();
    }



}
