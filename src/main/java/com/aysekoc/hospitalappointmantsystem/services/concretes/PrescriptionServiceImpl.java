package com.aysekoc.hospitalappointmantsystem.services.concretes;

import com.aysekoc.hospitalappointmantsystem.entities.Prescription;
import com.aysekoc.hospitalappointmantsystem.entities.User;
import com.aysekoc.hospitalappointmantsystem.repositories.PrescriptionRepository;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.PrescriptionService;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.UserService;
import com.aysekoc.hospitalappointmantsystem.services.dtos.PrescriptionDto.CreatePrescription;
import com.aysekoc.hospitalappointmantsystem.services.dtos.PrescriptionDto.PrescriptionListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {
    private final PrescriptionRepository prescriptionRepository;
    private final UserService userService;

    @Override
    @Transactional
    public void create(CreatePrescription createPrescription) {
        User user = userService.findById(createPrescription.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Prescription prescription = new Prescription();
        prescription.setMedicineName(createPrescription.getMedicineName());
        prescription.setDiagnosis(createPrescription.getDiagnosis());
        prescription.setUser(user);
        prescription.setHashPrescription(hash());
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
    @Transactional
    public void deleteById(Long id) {
        Prescription ent = prescriptionRepository.findById(id).orElseThrow(() -> new RuntimeException("Prescription not found"));
        prescriptionRepository.delete(ent);
    }

    @Override
    public Optional<Prescription> findByHashPrescription(String hash) {
        return prescriptionRepository.findByHashPrescription(hash);
    }


    @Override
    public Page<PrescriptionListDto> findById(Long id, Pageable pageable) {
        Page<Prescription> prescriptions = prescriptionRepository.findByUserId(id, pageable);
        return prescriptions.map(prescription -> new PrescriptionListDto(
                prescription.getId(),
                prescription.getMedicineName(),
                prescription.getDiagnosis(),
                prescription.getDate(),
                prescription.getHashPrescription(),
                prescription.getUser().getId()
        ));
    }

    private String hash() {
        Random random = new Random();
        int code = random.nextInt(9000) + 1000; // 1000 - 9999 arası
        return String.valueOf(code);
    }

}
