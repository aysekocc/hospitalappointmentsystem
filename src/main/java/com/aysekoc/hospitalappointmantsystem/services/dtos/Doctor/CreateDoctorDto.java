package com.aysekoc.hospitalappointmantsystem.services.dtos.Doctor;

import com.aysekoc.hospitalappointmantsystem.entities.Specialty;
import com.aysekoc.hospitalappointmantsystem.entities.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
