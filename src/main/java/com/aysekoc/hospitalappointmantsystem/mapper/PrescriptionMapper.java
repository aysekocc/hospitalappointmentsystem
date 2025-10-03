package com.aysekoc.hospitalappointmantsystem.mapper;

import com.aysekoc.hospitalappointmantsystem.entities.Appointment;
import com.aysekoc.hospitalappointmantsystem.entities.Prescription;
import com.aysekoc.hospitalappointmantsystem.entities.User;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.AppointmentService;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.UserService;
import com.aysekoc.hospitalappointmantsystem.services.concretes.UserServiceImpl;
import com.aysekoc.hospitalappointmantsystem.services.dtos.PrescriptionDto.CreatePrescription;
import com.aysekoc.hospitalappointmantsystem.services.dtos.PrescriptionDto.PrescriptionListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Component
public class PrescriptionMapper {

    private final AppointmentService appointmentService;
    private final UserService userService;

    public Prescription createMap(CreatePrescription createPrescription){
        Appointment appointment = appointmentService.findById(createPrescription.getAppointmentId())
                .orElseThrow(() -> new RuntimeException("Appointment not found with id " + createPrescription.getAppointmentId()));

        Prescription prescription = new Prescription();
        prescription.setDate(LocalDateTime.now());
        prescription.setMedicineName(createPrescription.getMedicineName());
        prescription.setDiagnosis(createPrescription.getDiagnosis());
        prescription.setAppointment(appointment);
        prescription.setHashPrescription(generateHash());

        User user = userService.findById(createPrescription.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id " + createPrescription.getUserId()));
        prescription.setUser(user);
        return prescription;
    }

    private String generateHash() {
        return String.valueOf((int)(Math.random() * 9000 + 1000));
    }

    public static PrescriptionListDto toDto(Prescription prescription){
        return  new PrescriptionListDto(
                prescription.getId(),
                prescription.getMedicineName(),
                prescription.getDiagnosis(),
                prescription.getDate(),
                prescription.getHashPrescription(),
                prescription.getUser() != null ? prescription.getUser().getId() : null

        );
    }
}

