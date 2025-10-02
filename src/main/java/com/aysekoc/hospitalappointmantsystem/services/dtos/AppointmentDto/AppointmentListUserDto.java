package com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto;

import com.aysekoc.hospitalappointmantsystem.entities.Clinic;
import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppointmentListUserDto {
    private Long id;
    private LocalDateTime startedDate;
    private LocalDateTime endedDate;
    private String userName;
    private String doctorName;
    private String hospitalName;
    private String title;
    private Clinic clinic;

}
