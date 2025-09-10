package com.aysekoc.hospitalappointmantsystem.mapper;

import com.aysekoc.hospitalappointmantsystem.entities.Hospital;
import com.aysekoc.hospitalappointmantsystem.services.dtos.HospitalDto.HospitalListDto;
import com.aysekoc.hospitalappointmantsystem.services.dtos.doctor.DoctorListDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HospitalMapper {

    public  HospitalListDto toDto(Hospital hospital) {
        List<DoctorListDto> doctorDtos = hospital.getDoctors().stream()
                .map(doctor -> new DoctorListDto(
                        doctor.getName(),
                        doctor.getSurname(),
                        doctor.getSpecialty(),
                        doctor.getTitle()
                )).toList();

        return new HospitalListDto(
                hospital.getId(),
                hospital.getName(),
                hospital.getTown(),
                hospital.getTownship(),
                hospital.getClinic(),
                doctorDtos
        );
    }
}
