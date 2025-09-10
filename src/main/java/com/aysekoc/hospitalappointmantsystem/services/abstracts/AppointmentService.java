package com.aysekoc.hospitalappointmantsystem.services.abstracts;

import com.aysekoc.hospitalappointmantsystem.entities.Appointment;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.AppointmentListDoctorDto;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.AppointmentListUserDto;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.CreateAppointment;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentService {
    Page<Appointment> getAppointments(int pageNumber, int pageSize);
    String createAppointment(CreateAppointment createAppointment);
    Optional<Appointment> findById(Long id);
    List<Appointment> findByStartDate(LocalDateTime startDate);
    List<Appointment> findByUsername(String username);
    List<Appointment> findByEndDate(LocalDateTime endDate);
    List<Appointment> findAll();
    void deleteAppointment(Long id);
    List<AppointmentListUserDto> userList(Long userId);
    List<AppointmentListDoctorDto> doctorList(Long doctorId);



}
