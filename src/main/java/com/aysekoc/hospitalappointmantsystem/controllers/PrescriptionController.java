package com.aysekoc.hospitalappointmantsystem.controllers;


import com.aysekoc.hospitalappointmantsystem.entities.Prescription;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.PrescriptionService;
import com.aysekoc.hospitalappointmantsystem.services.dtos.PrescriptionDto.CreatePrescription;
import com.aysekoc.hospitalappointmantsystem.services.dtos.PrescriptionDto.PrescriptionListDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/prescription")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5198")
public class PrescriptionController {
    private final PrescriptionService prescriptionService;


    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @PostMapping("/create")
    public void createPrescription(@RequestBody CreatePrescription createPrescription) {
        System.out.println("Backend'e gelen prescription DTO: " + createPrescription);
        prescriptionService.create(createPrescription);
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

    @GetMapping("/list/by-appointment/{appointmentId}")
    @PreAuthorize("hasAnyRole('ROLE_DOCTOR','ROLE_USER')")
    public ResponseEntity<PrescriptionListDto> getByAppointment(@PathVariable Long appointmentId) {
        Optional<PrescriptionListDto> prescription = prescriptionService.findByAppointmentId(appointmentId);
        return prescription.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok(null));
    }

    @PreAuthorize("hasRole('ROLE_DOCTOR')")
    @DeleteMapping("/{id}")
    public void deleteById(@PathVariable Long id){
        prescriptionService.deleteById(id);
    }
}