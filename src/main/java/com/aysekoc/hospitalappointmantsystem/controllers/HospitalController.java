package com.aysekoc.hospitalappointmantsystem.controllers;


import com.aysekoc.hospitalappointmantsystem.entities.Hospital;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.HospitalService;
import com.aysekoc.hospitalappointmantsystem.services.dtos.HospitalDto.CreateHospital;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/hospital")
@RequiredArgsConstructor
public class HospitalController {

    @Autowired
    private final HospitalService hospitalService;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public void createByHospital(@RequestBody CreateHospital createHospital){
        hospitalService.createByHospital(createHospital);
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DOCTOR','ROLE_USER')")
    @GetMapping("/list/name")
    public List<Hospital> findByHospitalName(@RequestParam CreateHospital createHospital){
        return hospitalService.findByHospitalName(createHospital);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/hospital")
    public void updateByHospital(@RequestBody CreateHospital createHospital, @RequestParam Long id){
        hospitalService.updateHospital(id,createHospital);
    }


    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DOCTOR','ROLE_USER')")
    @GetMapping("/list/Allhospital")
    public ResponseEntity<Page<Hospital>> getHospitals(@RequestParam(defaultValue = "0") int pageNumber, @RequestParam(defaultValue = "10") int pageSize){
        Page<Hospital> hospitals = hospitalService.getHospitals(pageNumber, pageSize);
        return ResponseEntity.ok(hospitals);
    }


    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping
    public void deleteByHospital(@RequestParam Long id){
        hospitalService.deleteByHospital(id);
    }


}
