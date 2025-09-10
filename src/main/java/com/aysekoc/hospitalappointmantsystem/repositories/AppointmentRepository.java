package com.aysekoc.hospitalappointmantsystem.repositories;

import com.aysekoc.hospitalappointmantsystem.entities.Appointment;
import com.aysekoc.hospitalappointmantsystem.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByUser(User user);

    List<Appointment> findByEndedDate(LocalDateTime endedDate);

    List<Appointment> findByStartedDate(LocalDateTime startedDate);
    List<Appointment> findByUserId(Long userId);
    List<Appointment> findByDoctorId(Long doctorId);
}
