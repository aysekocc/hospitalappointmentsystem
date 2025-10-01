package com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDto {
    private Long id;
    private String username;
    private String doctorName;
    private String doctorSurname;
    private String doctorTitle;
    private LocalDateTime startedDate;
    private LocalDateTime endedDate;
}

