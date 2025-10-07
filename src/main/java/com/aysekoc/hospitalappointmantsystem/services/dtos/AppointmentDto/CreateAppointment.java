package com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto;

import com.aysekoc.hospitalappointmantsystem.entities.Specialty;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateAppointment {

    private LocalDateTime startedDate;
    private LocalDateTime endedDate;
    private Long doctor;
    private Long userId;
    private Long hospitalId;
    private Specialty specialty;

}
