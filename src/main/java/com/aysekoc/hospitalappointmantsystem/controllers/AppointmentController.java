package com.aysekoc.hospitalappointmantsystem.controllers;
import com.aysekoc.hospitalappointmantsystem.entities.Appointment;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.AppointmentService;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.CreateAppointment;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/appointment")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5198")
public class AppointmentController {

    private final AppointmentService appointmentService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping("/create")
    public ResponseEntity<Void> createAppointment(@RequestBody CreateAppointment createAppointment) {
        appointmentService.createAppointment(createAppointment);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DOCTOR')")
    @GetMapping
    public ResponseEntity<Page<Appointment>> getAppointments(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(appointmentService.getAppointments(pageNumber, pageSize));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DOCTOR')")
    @GetMapping("/list/id")
    public ResponseEntity<Optional<Appointment>> findById(@RequestParam Long appointmentId) {
        return ResponseEntity.ok(appointmentService.findById(appointmentId));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DOCTOR')")
    @GetMapping("/list/startdate")
    public ResponseEntity<List<Appointment>> findByStartDate(@RequestParam LocalDateTime startDate) {
        return ResponseEntity.ok(appointmentService.findByStartDate(startDate));
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_DOCTOR')")
    @GetMapping("/list/enddate")
    public ResponseEntity<List<Appointment>> findByEndDate(@RequestParam LocalDateTime endDate) {
        return ResponseEntity.ok(appointmentService.findByEndDate(endDate));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long appointmentId) {
        appointmentService.deleteAppointment(appointmentId);
        return ResponseEntity.ok().build();
    }
}