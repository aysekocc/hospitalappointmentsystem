package com.aysekoc.hospitalappointmantsystem.services.dtos.Doctor;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorListDto {

    private String name;
    private String surname;
    private String specialty;
    private String title;
}
