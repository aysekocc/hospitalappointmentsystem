package com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppointmentListDoctorDto {
    private Long id;
    private LocalDateTime startedDate;
    private LocalDateTime endedDate;
    private Long doctorId;
    private String doctorName;
    private Long hospitalId;
    private String hospitalName;
    private String status;
    private String userUsername;
    private String doctorTitle;
}
