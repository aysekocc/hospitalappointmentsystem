package com.aysekoc.hospitalappointmantsystem.services.dtos.PrescriptionDto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreatePrescription {
    private String medicineName;
    private String diagnosis;

    private Long userId;
}

