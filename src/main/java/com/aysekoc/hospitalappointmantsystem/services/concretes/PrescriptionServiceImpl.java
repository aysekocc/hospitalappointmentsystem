package com.aysekoc.hospitalappointmantsystem.services.concretes;

import com.aysekoc.hospitalappointmantsystem.entities.Prescription;
import com.aysekoc.hospitalappointmantsystem.repositories.PrescriptionRepository;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.PrescriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;

    @Override
    public void create(Prescription prescription) {
        prescription.setId(prescription.getId());
        prescriptionRepository.save(prescription);
    }

    @Override
    public List<Prescription> findByDate(LocalDateTime date) {
        List<Prescription> prescription = prescriptionRepository.findByDate(date);
        if(prescription.isEmpty()){
            throw new NullPointerException("prescription is null");
        }
        return prescription;
    }

    @Override
    public void deleteById(UUID id) {
        prescriptionRepository.deleteById(id);
    }

    @Override
    public Optional<Prescription> findByHashPrescription(Prescription prescription) {
        return Optional.empty();
    }

    @Override
    public Page<Prescription> findByUserId(UUID userId, Pageable pageable) {
        Page<Prescription> prescription = prescriptionRepository.findByUserId(userId, pageable);
        return prescription;
    }


}
