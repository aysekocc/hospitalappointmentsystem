package com.aysekoc.hospitalappointmantsystem.repositories;

import com.aysekoc.hospitalappointmantsystem.entities.Hospital;
import com.aysekoc.hospitalappointmantsystem.services.dtos.HospitalDto.CreateHospital;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, UUID> {

    List<Hospital> findByName(CreateHospital createHospital);
    void deleteById(UUID id);


}
