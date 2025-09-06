package com.aysekoc.hospitalappointmantsystem.controllers;
import com.aysekoc.hospitalappointmantsystem.entities.Appointment;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.AppointmentService;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.CreateAppointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/appointment")

public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @PostMapping("/create")
    public void createAppointment(@RequestBody CreateAppointment createAppointment){
        appointmentService.createAppointment(createAppointment);
    }

    @GetMapping
    public ResponseEntity<Page<Appointment>> getAppointments(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        Page<Appointment> appointments = appointmentService.getAppointments(pageNumber, pageSize);
        return ResponseEntity.ok(appointments);
    }


    @GetMapping("/list/id")
    public Optional<Appointment> findById(UUID id){
        return appointmentService.findById(id);
    }

    @GetMapping("/list/startdate")
    public List<Appointment> findByStartDate(LocalDateTime startDate){
        return appointmentService.findByStartDate(startDate);
    }

    @GetMapping("/list/enddate")
    public List<Appointment> findByEndDate(LocalDateTime endDate){
        return appointmentService.findByEndDate(endDate);
    }

    @DeleteMapping()
    public void deleteAppointment(UUID id){
        appointmentService.deleteAppointment(id);
    }




}
