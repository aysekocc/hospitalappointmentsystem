package com.aysekoc.hospitalappointmantsystem.services.dtos.DoctorDto;

import com.aysekoc.hospitalappointmantsystem.entities.Specialty;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class DoctorListDto {
    @NotBlank()
    private String name;
    @NotBlank()
    private String surname;
    @NotBlank()
    private Specialty specialty;
    @NotBlank()
    private String title;

}
