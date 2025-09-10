package com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppointmentListDoctorDto {
    private LocalDateTime startedDate;
    private LocalDateTime endedDate;
    private Long doctor;
    private Long hospitalId;
    private String status;
}
