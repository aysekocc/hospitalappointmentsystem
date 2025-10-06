package com.aysekoc.hospitalappointmantsystem.services.dtos.HospitalDto;

import com.aysekoc.hospitalappointmantsystem.services.dtos.DoctorDto.DoctorListDto;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HospitalListDto {
    private Long id;
    private String name;
    private String town;
    private String township;
    private String clinic;
    private List<DoctorListDto> doctors ;
}
