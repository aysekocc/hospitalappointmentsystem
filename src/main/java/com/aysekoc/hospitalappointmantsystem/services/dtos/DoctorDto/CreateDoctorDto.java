package com.aysekoc.hospitalappointmantsystem.services.dtos.DoctorDto;

import com.aysekoc.hospitalappointmantsystem.entities.Specialty;
import com.aysekoc.hospitalappointmantsystem.entities.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateDoctorDto {

    @NotBlank(message = "not Empty")
    private String name;

    @NotBlank(message = "not Empty")
    private String surname;

    private Specialty specialty;

    private String title;

    private boolean gender;

    private String password;

    private String username;

    private int age;

    private Long appointmentId;

    private Long hospitalId;

    private Set<User> userId;


}
