package com.aysekoc.hospitalappointmantsystem.services.abstracts;


import com.aysekoc.hospitalappointmantsystem.entities.Hospital;
import com.aysekoc.hospitalappointmantsystem.services.dtos.HospitalDto.CreateHospital;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.UUID;

public interface HospitalService {

    void createByHospital(CreateHospital createHospital);
    List<Hospital> findByHospitalName(CreateHospital createHospital);
    Hospital updateHospital(Long id, CreateHospital createHospital);
    Page<Hospital> getHospitals(int pageNumber, int pageSize);
    void deleteByHospital(Long id);
    Hospital findById(Long Id);


}
