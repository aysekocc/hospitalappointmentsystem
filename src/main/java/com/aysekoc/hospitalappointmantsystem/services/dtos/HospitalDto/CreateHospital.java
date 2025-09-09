package com.aysekoc.hospitalappointmantsystem.services.dtos.HospitalDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateHospital {

    private String name;
    private String town;
    private String township;
    private String clinic;
    private List<Long> doctorsId;
}
