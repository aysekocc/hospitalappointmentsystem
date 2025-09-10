package com.aysekoc.hospitalappointmantsystem.mapper;

import com.aysekoc.hospitalappointmantsystem.entities.Doctor;

import com.aysekoc.hospitalappointmantsystem.services.concretes.HospitalServiceImpl;
import com.aysekoc.hospitalappointmantsystem.services.dtos.doctor.CreateDoctorDto;
import com.aysekoc.hospitalappointmantsystem.services.dtos.doctor.DoctorListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DoctorMapper {
    private final HospitalServiceImpl hospitalServiceImpl;

    public Doctor createDoctorMap(CreateDoctorDto doctor) {
        Doctor doctorEntity = new Doctor();
        doctorEntity.setName(doctor.getName());
        doctorEntity.setSurname(doctor.getSurname());
        doctorEntity.setAge(doctor.getAge());
        doctorEntity.setGender(doctor.isGender());
        doctorEntity.setSpecialty(doctor.getSpecialty());
        doctorEntity.setUserId(doctor.getUserId());
        doctorEntity.setHospital(hospitalServiceImpl.findByIdMap(doctor.getHospitalId()));
        return doctorEntity;
    }

    public DoctorListDto listDoctorDto(Doctor doctor) {
        DoctorListDto doctorListDto = new DoctorListDto();
        doctorListDto.setName(doctor.getName());
        doctorListDto.setSurname(doctor.getSurname());
        doctorListDto.setTitle(doctor.getTitle());
        doctorListDto.setSpecialty(doctor.getSpecialty());
        return doctorListDto;
    }
}
