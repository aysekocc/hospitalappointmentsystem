package com.aysekoc.hospitalappointmantsystem.mapper;

import com.aysekoc.hospitalappointmantsystem.entities.*;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.DoctorService;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.HospitalService;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.UserService;
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

    private final DoctorService doctorService;
    private final HospitalService hospitalService;
    private final UserService userService;

    public Appointment createAppointment(CreateAppointment createAppointment) {
        Appointment appointment = new Appointment();

        Doctor doctor = doctorService.findById(createAppointment.getDoctor())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
        appointment.setDoctor(doctor);

        Hospital hospital = hospitalService.findByIdMap(createAppointment.getHospitalId());
        appointment.setHospitalId(hospital);

        appointment.setStartedDate(createAppointment.getStartedDate());
        appointment.setEndedDate(createAppointment.getEndedDate());

        User user = userService.findById(createAppointment.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        appointment.setUser(user);


        return appointment;
    }

    public AppointmentListUserDto mapToUserDto(Appointment appt) {
        AppointmentListUserDto dto = new AppointmentListUserDto();

        dto.setId(appt.getId());
        dto.setStartedDate(appt.getStartedDate());
        dto.setEndedDate(appt.getEndedDate());
        dto.setUserName(
                appt.getUser() != null ? appt.getUser().getUsername() : "-"
        );
        if (appt.getDoctor() != null) {
            dto.setDoctorName(appt.getDoctor().getUsername());
            dto.setTitle(appt.getDoctor().getTitle());
        } else {
            dto.setDoctorName("-");
            dto.setTitle("-");
        }
        dto.setHospitalName(
                appt.getHospitalId() != null ? appt.getHospitalId().getName() : "-"
        );
        Prescription prescription = appt.getPrescription();
        if (prescription != null) {
            dto.setPrescriptionMedicineName(prescription.getMedicineName());
            dto.setPrescriptionDiagnosis(prescription.getDiagnosis());
            dto.setPrescriptionHash(prescription.getHashPrescription());
        } else {
            dto.setPrescriptionMedicineName("-");
            dto.setPrescriptionDiagnosis("-");
            dto.setPrescriptionHash("-");
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
            dto.setDoctorName(appt.getDoctor().getUsername());
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

