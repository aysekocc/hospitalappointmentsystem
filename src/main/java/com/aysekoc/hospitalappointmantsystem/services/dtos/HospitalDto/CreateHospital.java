package com.aysekoc.hospitalappointmantsystem.services.dtos.HospitalDto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateHospital {
    private UUID id;
    private String name;
    private String town;
    private String township;
}
