package com.aysekoc.hospitalappointmantsystem.services.dtos.Doctor;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateDoctorDto {
    private String name;
    private String surname;
    private String specialty;
    private String title;

    private boolean gender;
    private int age;
    private Long appointmentId;
    private Long hospitalId;

    private Set<User> userId;


}
