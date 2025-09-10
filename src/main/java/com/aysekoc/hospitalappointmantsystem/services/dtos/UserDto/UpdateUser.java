package com.aysekoc.hospitalappointmantsystem.services.dtos.UserDto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class UpdateUser {
    private Long Id;
    private String identity;
    private String name;
    private String surname;
    private int age;
    private String gender;
    private String username;
    private String password;

}
