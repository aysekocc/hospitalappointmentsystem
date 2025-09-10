package com.aysekoc.hospitalappointmantsystem.controllers;


import com.aysekoc.hospitalappointmantsystem.entities.Prescription;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.PrescriptionService;
import com.aysekoc.hospitalappointmantsystem.services.dtos.PrescriptionDto.PrescriptionListDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/prescription")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5198")
public class PrescriptionController {
    private final PrescriptionService prescriptionService;


    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @PostMapping("/create")
    public void createPrescription(@RequestBody Prescription prescription){
        prescription.setDate(LocalDateTime.now());
    }


    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @GetMapping("/list/hash")
    public Optional<Prescription> findByHashPrescription(@Valid @RequestParam String hash) {
        return prescriptionService.findByHashPrescription(hash);
    }

    @PreAuthorize("hasAnyRole('ROLE_DOCTOR','ROLE_USER')")
    @GetMapping("/list/user")
    public Page<PrescriptionListDto> findById(@Valid @RequestParam Long userId, Pageable pageable) {
        return prescriptionService.findById(userId, pageable);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DOCTOR')")
    @DeleteMapping()
    public void deleteById(Long id){
        prescriptionService.deleteById(id);
    }
}
