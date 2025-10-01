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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
        Optional<Appointment> appointment = appointmentRepository.findById(appointmentId);
        if (appointment.isEmpty()) {
            throw new RuntimeException("Appointment not found");
        }
        return appointment;
    }

    @Override
    public List<Appointment> findByStartDate(LocalDateTime startDate) {
        List<Appointment> appointments = appointmentRepository.findByStartedDate(startDate);
        return appointments;
    }

    @Override
    public List<Appointment> findByUsername(String username) {
        User user = userServiceImpl.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return appointmentRepository.findByUser(user);
    }

    @Override
    public List<Appointment> findByEndDate(LocalDateTime endDate) {
        List<Appointment> appointments = appointmentRepository.findByEndedDate(endDate);
        return appointments;
    }

    @Override
    public List<Appointment> findAll() {
        return appointmentRepository.findAll();
    }

    @Override
    public void deleteAppointment(Long appointmetnId) {
        appointmentRepository.deleteById(appointmetnId);

    }

    @Override
    public List<AppointmentListUserDto> userList(Long userId) {
        List<Appointment> appointments = appointmentRepository.findByUserId(userId);
        return appointments.stream()
                .map(appointmentMapper::mapToUserDto).toList();
    }

    @Override
    public List<AppointmentListDoctorDto> doctorList(Long doctorId){
        List<Appointment> appointments = appointmentRepository.findByUserId(doctorId);
        if(appointments.isEmpty()){
            throw new RuntimeException("Doctor not found");
        }
        return appointments.stream()
                .map(appointmentMapper::mapToDoctorDto).toList();
    }

}
