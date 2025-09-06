package com.aysekoc.hospitalappointmantsystem.services.dtos.UserDto;
import com.aysekoc.hospitalappointmantsystem.entities.Roles;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {


    private String identity;
    private String name;
    private String surname;
    private int age;
    private String gender;
    private String username;
    private String password;
    private String role;


}
