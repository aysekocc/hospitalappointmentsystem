package com.aysekoc.hospitalappointmantsystem.services.concretes;

import com.aysekoc.hospitalappointmantsystem.entities.Hospital;
import com.aysekoc.hospitalappointmantsystem.repositories.HospitalRepository;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.HospitalService;
import com.aysekoc.hospitalappointmantsystem.services.dtos.HospitalDto.CreateHospital;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private final HospitalRepository hospitalRepository;


    @Override
    public Page<Hospital> getHospitals(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return hospitalRepository.findAll(pageable);
    }

    @Override
    public void createByHospital(CreateHospital createHospital) {
        Hospital hospital = new Hospital();
        hospital.setId(createHospital.getId());
        hospital.setName(createHospital.getName());
        hospital.setTown(createHospital.getTown());
        hospital.setTownship(createHospital.getTownship());
        hospitalRepository.save(hospital);
    }

    @Override
    public List<Hospital> findByHospitalName(CreateHospital createHospital) {
        List<Hospital> hospital = hospitalRepository.findByName(createHospital);
        if (hospital.isEmpty()) {
            throw new NullPointerException("Hospital not found");
        }
        return hospital;
    }

    @Override
    public Hospital updateHospital(UUID id, CreateHospital createHospital) {
        Hospital existing = hospitalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Hospital not found with id: " + id));
        existing.setName(createHospital.getName());
        existing.setTown(createHospital.getTown());
        existing.setTownship(createHospital.getTownship());
        return hospitalRepository.save(existing);
    }



    @Override
    public void deleteByHospital(UUID id) {
        hospitalRepository.deleteById(id);

    }




}
