package com.aysekoc.hospitalappointmantsystem.controllers;
import com.aysekoc.hospitalappointmantsystem.entities.Appointment;
import com.aysekoc.hospitalappointmantsystem.entities.Doctor;
import com.aysekoc.hospitalappointmantsystem.entities.User;
import com.aysekoc.hospitalappointmantsystem.mapper.AppointmentMapper;
import com.aysekoc.hospitalappointmantsystem.repositories.UserRepository;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.AppointmentService;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.DoctorService;
import com.aysekoc.hospitalappointmantsystem.services.concretes.UserServiceImpl;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.AppointmentListDoctorDto;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.AppointmentListUserDto;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.CreateAppointment;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/appointment")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5198")
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final UserRepository userRepository;
    private final DoctorService doctorService;
    private final AppointmentMapper appointmentMapper;
    private final UserServiceImpl userServiceImpl;

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_DOCTOR')")
    @PostMapping("/create")
    public ResponseEntity<Void> createAppointment(
            @Valid @RequestBody CreateAppointment createAppointment,
            @AuthenticationPrincipal UserDetails userDetails) {

        User user = userServiceImpl.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        createAppointment.setUserId(user.getId());
        appointmentService.createAppointment(createAppointment);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_DOCTOR')")
    @GetMapping
    public ResponseEntity<Page<Appointment>> getAppointments(
            @Valid
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ResponseEntity.ok(appointmentService.getAppointments(pageNumber, pageSize));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_DOCTOR')")
    @GetMapping("/list/id")
    public ResponseEntity<Optional<Appointment>> findById(@Valid @RequestParam Long appointmentId) {
        return ResponseEntity.ok(appointmentService.findById(appointmentId));
    }


    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_DOCTOR')")
    @GetMapping("/list/startdate")
    public ResponseEntity<List<Appointment>> findByStartDate(@Valid @RequestParam LocalDate startDate) {
        return ResponseEntity.ok(appointmentService.findByStartDate(startDate));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_DOCTOR')")
    @GetMapping("/list/enddate")
    public ResponseEntity<List<Appointment>> findByEndDate(@Valid @RequestParam LocalDate endDate) {
        return ResponseEntity.ok(appointmentService.findByEndDate(endDate));
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_DOCTOR')")
    @GetMapping("/my-appointments")
    public ResponseEntity<List<Appointment>> getUserAppointments(@Valid Authentication authentication) {
        String username = authentication.getName();
        List<Appointment> appointments = appointmentService.findByUsername(username);
        return ResponseEntity.ok(appointments);
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_DOCTOR')")
    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<Void> deleteAppointment(@Valid @PathVariable Long appointmentId) {
        appointmentService.deleteAppointment(appointmentId);
        return ResponseEntity.ok().build();
    }
}