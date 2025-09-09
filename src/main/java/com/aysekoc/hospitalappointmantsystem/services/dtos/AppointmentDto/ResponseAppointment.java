package com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseAppointment {
    private Long id;
    private LocalDateTime startedDate;
    private LocalDateTime endedDate;
    private String status;
    private String patientName; // user.name + user.surname
    private String doctorName;  // doctor.name + doctor.surname

}
