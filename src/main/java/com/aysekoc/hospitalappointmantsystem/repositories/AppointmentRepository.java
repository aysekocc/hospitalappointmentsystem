package com.aysekoc.hospitalappointmantsystem.repositories;

import com.aysekoc.hospitalappointmantsystem.entities.Appointment;
import com.aysekoc.hospitalappointmantsystem.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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


    @Query("SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END " +
            "FROM Appointment a " +
            "WHERE a.doctor.id = :doctorId " +
            "AND a.startedDate < :endedDate " +
            "AND a.endedDate > :startedDate")
    boolean existsConflict(
            @Param("doctorId") Long doctorId,
            @Param("startedDate") LocalDateTime startedDate,
            @Param("endedDate") LocalDateTime endedDate
    );

    @Query("SELECT a.startedDate FROM Appointment a WHERE a.doctor.id = :doctorId AND a.startedDate BETWEEN :start AND :end")
    List<LocalDateTime> findByDoctorAndDateRange(@Param("doctorId") Long doctorId,
                                                 @Param("start") LocalDateTime start,
                                                 @Param("end") LocalDateTime end);

}
