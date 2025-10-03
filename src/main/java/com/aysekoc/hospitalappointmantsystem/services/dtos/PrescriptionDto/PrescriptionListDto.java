package com.aysekoc.hospitalappointmantsystem.services.dtos.PrescriptionDto;

import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PrescriptionListDto {
    private Long id;
    private String medicineName;
    private String diagnosis;
    private LocalDateTime date;
    private String hashPrescription;
    private Long userId;


}
