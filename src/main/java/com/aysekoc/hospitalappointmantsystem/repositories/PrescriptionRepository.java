package com.aysekoc.hospitalappointmantsystem.repositories;

import com.aysekoc.hospitalappointmantsystem.entities.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrescriptionRepository extends JpaRepository<Prescription, Long> {

    List<Prescription> findByDate(LocalDateTime date);
    Page<Prescription> findByUserId(Long id, Pageable pageable);
    Optional<Prescription> findByHashPrescription(String hash);
    List<Prescription> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    Optional<Prescription> findByAppointmentId(Long appointmentId);

}
