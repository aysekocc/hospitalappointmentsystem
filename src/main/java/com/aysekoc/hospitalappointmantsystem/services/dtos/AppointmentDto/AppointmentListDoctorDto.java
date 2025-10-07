package com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto;

import com.aysekoc.hospitalappointmantsystem.services.dtos.PrescriptionDto.PrescriptionListDto;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AppointmentListDoctorDto {
    private Long id;
    private LocalDateTime startedDate;
    private LocalDateTime endedDate;
    private String username;
    private String hospitalName;
    private String doctorTitle;
    private String doctorName;
    private Long doctorId;
    private Long hospitalId;
    private String town;
    private String townShip;
    private List<PrescriptionListDto> prescriptions;


}
