package com.aysekoc.hospitalappointmantsystem.services.concretes;

import com.aysekoc.hospitalappointmantsystem.entities.Doctor;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HospitalServiceImpl implements HospitalService {

    @Autowired
    private final HospitalRepository hospitalRepository;
    private final DoctorServiceImpl doctorService;


    @Override
    public Page<Hospital> getHospitals(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return hospitalRepository.findAll(pageable);
    }

    @Override
    public void createByHospital(CreateHospital createHospital) {
        Hospital hospital = new Hospital();
        List<Doctor> dc = new ArrayList<>();
        for (int i = 0; i < createHospital.getDoctorsId().size(); i++) {
            dc.add(doctorService.findById(createHospital.getDoctorsId().get(i)).get());
        }
        hospital.setDoctors(dc);
        hospital.setName(createHospital.getName());
        hospital.setTown(createHospital.getTown());
        hospital.setTownship(createHospital.getTownship());
        hospital.setClinic(createHospital.getClinic());

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
    public Hospital updateHospital(Long appointmentId, CreateHospital createHospital) {
        Hospital existing = hospitalRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Hospital not found with id: " + appointmentId));

        existing.setName(createHospital.getName());
        existing.setTown(createHospital.getTown());
        existing.setTownship(createHospital.getTownship());

        return hospitalRepository.save(existing);
    }


    @Override
    public void deleteByHospital(Long appointmentId) {
        hospitalRepository.deleteById(appointmentId);

    }
    @Override
     public  Hospital findById(Long Id){
      return   hospitalRepository.findById(Id).orElseThrow(() -> new RuntimeException("Hospital not found"));
    }


}
