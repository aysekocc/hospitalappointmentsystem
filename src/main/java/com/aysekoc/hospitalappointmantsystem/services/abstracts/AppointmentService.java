package com.aysekoc.hospitalappointmantsystem.services.abstracts;

import com.aysekoc.hospitalappointmantsystem.entities.Appointment;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.CreateAppointment;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AppointmentService {


    Page<Appointment> getAppointments(int pageNumber, int pageSize);

    String createAppointment(CreateAppointment createAppointment);
    void deleteAppointment(Long id);
    Optional<Appointment> findById(Long id);
    List<Appointment> findByStartDate(LocalDateTime startDate);
    List<Appointment> findByEndDate(LocalDateTime endDate);

}
