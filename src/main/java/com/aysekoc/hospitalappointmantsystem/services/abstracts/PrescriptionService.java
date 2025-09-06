package com.aysekoc.hospitalappointmantsystem.services.abstracts;

import com.aysekoc.hospitalappointmantsystem.entities.Prescription;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PrescriptionService {
    void create (Prescription prescription);
    List<Prescription> findByDate(LocalDateTime date);
    void deleteById (UUID id);
    Optional<Prescription> findByHashPrescription(Prescription prescription);
    Page<Prescription> findByUserId(UUID userId, Pageable pageable);

}
