package com.aysekoc.hospitalappointmantsystem.mapper;

import com.aysekoc.hospitalappointmantsystem.entities.Appointment;
import com.aysekoc.hospitalappointmantsystem.entities.Doctor;
import com.aysekoc.hospitalappointmantsystem.entities.Hospital;
import com.aysekoc.hospitalappointmantsystem.entities.User;
import com.aysekoc.hospitalappointmantsystem.services.concretes.DoctorServiceImpl;
import com.aysekoc.hospitalappointmantsystem.services.concretes.HospitalServiceImpl;
import com.aysekoc.hospitalappointmantsystem.services.concretes.UserServiceImpl;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.AppointmentListDoctorDto;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.AppointmentListUserDto;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.CreateAppointment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AppointmentMapper {
    private final DoctorServiceImpl doctorServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private final HospitalServiceImpl hospitalServiceImpl;


    public Appointment createAppointment(CreateAppointment createAppointment) {
        Appointment appointment = new Appointment();
        Doctor doctor = doctorServiceImpl.findById(createAppointment.getDoctor()).orElseThrow(() -> new RuntimeException("Doctor not found"));
        appointment.setDoctor(doctor);
        //User user = userServiceImpl.findById(createAppointment.getUser());
        //appointment.setUser(user);
        Hospital host = hospitalServiceImpl.findByIdMap(createAppointment.getHospitalId());
        appointment.setHospitalId(host);
        appointment.setStatus(createAppointment.getStatus());
        appointment.setStartedDate(createAppointment.getStartedDate());
        appointment.setEndedDate(createAppointment.getEndedDate());
        return appointment;
    }

    public AppointmentListUserDto mapToUserDto(Appointment appt) {
        AppointmentListUserDto dto = new AppointmentListUserDto();
        dto.setId(appt.getId());
        dto.setStartedDate(appt.getStartedDate());
        dto.setEndedDate(appt.getEndedDate());
        dto.setUserName(appt.getUser().getName());
        dto.setHospitalName(appt.getHospitalId().getName());
        dto.setTown(appt.getHospitalId().getTown());
        dto.setTownship(appt.getHospitalId().getTownship());
        dto.setTitle(appt.getDoctor().getTitle());
        dto.setStatus(appt.getStatus());
        dto.setDoctorName(appt.getDoctor().getName());
        return dto;
    }

    public AppointmentListDoctorDto mapToDoctorDto(Appointment appt) {
        AppointmentListDoctorDto dto = new AppointmentListDoctorDto();
        dto.setId(appt.getId());
        dto.setStartedDate(appt.getStartedDate());
        dto.setEndedDate(appt.getEndedDate());
        dto.setDoctorId(appt.getDoctor().getId());
        dto.setDoctorName(appt.getDoctor().getName());
        dto.setDoctorTitle(appt.getDoctor().getTitle());
        dto.setHospitalId(appt.getHospitalId().getId());
        dto.setHospitalName(appt.getHospitalId().getName());
        dto.setStatus(appt.getStatus());
        dto.setUserUsername(appt.getUser().getUsername());

        return dto;
    }
}
