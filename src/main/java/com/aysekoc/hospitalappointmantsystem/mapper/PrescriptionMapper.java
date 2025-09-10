package com.aysekoc.hospitalappointmantsystem.mapper;

import com.aysekoc.hospitalappointmantsystem.entities.Prescription;
import com.aysekoc.hospitalappointmantsystem.entities.User;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.UserService;
import com.aysekoc.hospitalappointmantsystem.services.dtos.PrescriptionDto.CreatePrescription;
import com.aysekoc.hospitalappointmantsystem.services.dtos.PrescriptionDto.PrescriptionListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;

@RequiredArgsConstructor
@Component
public class PrescriptionMapper {

    private final UserService userService;

    public  Prescription createMap(CreatePrescription createPrescription){
        User user = userService.findById(createPrescription.getUserId());
        Prescription prescription = new Prescription();
        prescription.setDate(LocalDateTime.now());
        prescription.setMedicineName(createPrescription.getMedicineName());
        prescription.setDiagnosis(createPrescription.getDiagnosis());
        prescription.setUser(user);
        prescription.setHashPrescription(hash());
        return prescription;
    }
    private String hash() {
        Random random = new Random();
        int code = random.nextInt(9000) + 1000; // 1000 - 9999 arası
        return String.valueOf(code);
    }
    public static PrescriptionListDto toDto(Prescription prescription){
        return  new PrescriptionListDto(
                prescription.getId(),
                prescription.getMedicineName(),
                prescription.getDiagnosis(),
                prescription.getDate(),
                prescription.getHashPrescription(),
                prescription.getUser().getId()
        );
    }
}
