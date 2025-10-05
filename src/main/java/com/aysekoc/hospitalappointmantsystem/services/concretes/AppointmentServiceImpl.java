package com.aysekoc.hospitalappointmantsystem.services.concretes;

import com.aysekoc.hospitalappointmantsystem.entities.Appointment;
import com.aysekoc.hospitalappointmantsystem.entities.User;
import com.aysekoc.hospitalappointmantsystem.mapper.AppointmentMapper;
import com.aysekoc.hospitalappointmantsystem.repositories.AppointmentRepository;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.AppointmentService;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.AppointmentListDoctorDto;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.AppointmentListUserDto;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.CreateAppointment;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentServiceImpl implements AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorServiceImpl doctorServiceImpl;
    private final UserServiceImpl userServiceImpl;
    private final HospitalServiceImpl hospitalServiceImpl;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository, DoctorServiceImpl doctorServiceImpl, UserServiceImpl userServiceImpl, HospitalServiceImpl hospitalServiceImpl) {
        this.appointmentRepository = appointmentRepository;
        this.doctorServiceImpl = doctorServiceImpl;
        this.userServiceImpl = userServiceImpl;
        this.hospitalServiceImpl = hospitalServiceImpl;
    }


    public Page<Appointment> getAppointments(int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return appointmentRepository.findAll(pageable);
    }

    @Override
    public String createAppointment(CreateAppointment createAppointment) {
        appointmentRepository.save(appointmentMapper.createAppointment(createAppointment));
        return "Created";
    }

    @Override
    public Optional<Appointment> findById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId);
    }

    @Override
    public List<Appointment> findByStartDate(LocalDate startDate) {
        return appointmentRepository.findByStartedDate(startDate);
    }

    @Override
    public List<Appointment> findByUsername(String username) {
        User user = userServiceImpl.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return appointmentRepository.findByUser(user);
    }

    @Override
    public List<Appointment> findByEndDate(LocalDate endDate) {
        return appointmentRepository.findByEndedDate(endDate);
    }

    @Override
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public List<AppointmentListUserDto> userList(Long userId) {
        List<Appointment> appointments = appointmentRepository.findByUserId(userId);
        if (appointments == null || appointments.isEmpty()) return Collections.emptyList();
        return appointments.stream()
                .map(appointmentMapper::mapToUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<AppointmentListDoctorDto> getAppointmentsByDoctorId(Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        if (appointments == null || appointments.isEmpty()) return Collections.emptyList();
        return appointments.stream()
                .map(appointmentMapper::mapToDoctorDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Appointment> findByDoctorId(Long doctorId) {
        List<Appointment> appointments = appointmentRepository.findByDoctorId(doctorId);
        return appointments != null ? appointments : Collections.emptyList();
    }

    @Override
    @Transactional
    public void deleteAppointment(Long appointmentId) {
        if (!appointmentRepository.existsById(appointmentId)) {
            throw new RuntimeException("Appointment not found with id: " + appointmentId);
        }
        appointmentRepository.deleteById(appointmentId);
    }
}
