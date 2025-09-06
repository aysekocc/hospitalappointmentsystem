package com.aysekoc.hospitalappointmantsystem.controllers;


import com.aysekoc.hospitalappointmantsystem.entities.Prescription;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/prescription")
@RequiredArgsConstructor
public class PrescriptionController {
    private final PrescriptionService prescriptionService;
    
    
    @PostMapping("/create")
    public void createPrescription(@RequestBody Prescription prescription){
        prescription.setDate(LocalDateTime.now());
    }


    @GetMapping("/list/hash")
    public Optional<Prescription> findByHashPrescription(@RequestParam Prescription prescription) {
        return prescriptionService.findByHashPrescription(prescription);
    }
    @GetMapping("/list/date")
    public Page<Prescription> findByUserId(@RequestParam UUID userId, Pageable pageable) {
        return prescriptionService.findByUserId(userId, pageable);
    }

    @DeleteMapping()
    public void deleteById(UUID id){
        prescriptionService.deleteById(id);
    }
}
