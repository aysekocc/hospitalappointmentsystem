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
    private final HospitalServiceImpl hospitalServiceImpl;
    private final UserServiceImpl userServiceImpl;

    public Appointment createAppointment(CreateAppointment createAppointment) {
        Appointment appointment = new Appointment();

        Doctor doctor = doctorServiceImpl.findById(createAppointment.getDoctor())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        appointment.setDoctor(doctor);

        Hospital hospital = hospitalServiceImpl.findByIdMap(createAppointment.getHospitalId());
        appointment.setHospitalId(hospital);

        appointment.setStartedDate(createAppointment.getStartedDate());
        appointment.setEndedDate(createAppointment.getEndedDate());

        User user = userServiceImpl.findById(createAppointment.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        appointment.setUser(user);


        return appointment;
    }

    public AppointmentListUserDto mapToUserDto(Appointment appt) {
        AppointmentListUserDto dto = new AppointmentListUserDto();
        dto.setId(appt.getId());
        dto.setStartedDate(appt.getStartedDate());
        dto.setEndedDate(appt.getEndedDate());

        if (appt.getUser() != null) {
            dto.setUserName(appt.getUser().getUsername());
        } else {
            dto.setUserName("bu");
        }

        if (appt.getDoctor() != null) {
            dto.setDoctorName(appt.getDoctor().getName());
            dto.setTitle(appt.getDoctor().getTitle());
        } else {
            dto.setDoctorName("-");
            dto.setTitle("-");
        }

        if (appt.getHospitalId() != null) {
            dto.setHospitalName(appt.getHospitalId().getName());
        } else {
            dto.setHospitalName("-");
        }

        return dto;
    }


    public AppointmentListDoctorDto mapToDoctorDto(Appointment appt) {
        AppointmentListDoctorDto dto = new AppointmentListDoctorDto();
        dto.setId(appt.getId());
        dto.setStartedDate(appt.getStartedDate());
        dto.setEndedDate(appt.getEndedDate());

        if (appt.getDoctor() != null) {
            dto.setDoctorId(appt.getDoctor().getId());
            dto.setDoctorName(appt.getDoctor().getName());
            dto.setDoctorTitle(appt.getDoctor().getTitle());
        } else {
            dto.setDoctorName("-");
            dto.setDoctorTitle("-");
        }

        if (appt.getHospitalId() != null) {
            dto.setHospitalId(appt.getHospitalId().getId());
            dto.setHospitalName(appt.getHospitalId().getName());
        } else {
            dto.setHospitalName("-");
        }

        if (appt.getUser() != null) {
            dto.setUsername(appt.getUser().getUsername());
        } else {
            dto.setUsername("bulunamadı");
        }

        return dto;
    }

}

