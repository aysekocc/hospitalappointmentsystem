package com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto;

import com.aysekoc.hospitalappointmantsystem.entities.Specialty;
import lombok.*;

import java.time.LocalDate;
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
    private String prescriptionMedicineName;
    private String prescriptionDiagnosis;
    private String prescriptionHash;
    private Specialty specialty;


}
