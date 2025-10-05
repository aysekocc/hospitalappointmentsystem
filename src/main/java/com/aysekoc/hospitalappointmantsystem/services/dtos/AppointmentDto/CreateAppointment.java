package com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto;

import com.aysekoc.hospitalappointmantsystem.entities.Specialty;
import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateAppointment {
    private LocalDate startedDate;
    private LocalDate endedDate;
    private Long doctor;
    private Long userId;
    private Long hospitalId;
    private Specialty specialty;

}
