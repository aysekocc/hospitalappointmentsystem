package com.aysekoc.hospitalappointmantsystem.services.abstracts;

import com.aysekoc.hospitalappointmantsystem.entities.Prescription;
import com.aysekoc.hospitalappointmantsystem.services.dtos.PrescriptionDto.CreatePrescription;
import com.aysekoc.hospitalappointmantsystem.services.dtos.PrescriptionDto.PrescriptionListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PrescriptionService {
    void create (CreatePrescription createPrescription);
    List<Prescription> findByDate(LocalDateTime date);
    void deleteById (Long id);
    Optional<Prescription> findByHashPrescription(String hash);
    Page<PrescriptionListDto> findById(Long id, Pageable pageable);

}
