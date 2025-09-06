package com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateAppointment {

    private UUID id;
    private LocalDateTime startedDate;
    private LocalDateTime endedDate;

}
