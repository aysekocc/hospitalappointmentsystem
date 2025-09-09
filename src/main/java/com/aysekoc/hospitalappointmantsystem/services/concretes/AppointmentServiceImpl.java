package com.aysekoc.hospitalappointmantsystem.services.concretes;

import com.aysekoc.hospitalappointmantsystem.entities.Appointment;
import com.aysekoc.hospitalappointmantsystem.entities.Doctor;
import com.aysekoc.hospitalappointmantsystem.entities.Hospital;
import com.aysekoc.hospitalappointmantsystem.entities.User;
import com.aysekoc.hospitalappointmantsystem.repositories.AppointmentRepository;
import com.aysekoc.hospitalappointmantsystem.services.abstracts.AppointmentService;
import com.aysekoc.hospitalappointmantsystem.services.dtos.AppointmentDto.CreateAppointment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public String  createAppointment(CreateAppointment createAppointment) {
        Appointment appointment = new Appointment();
        Doctor doctor = doctorServiceImpl.findById(createAppointment.getDoctor()).orElseThrow();
        appointment.setDoctor(doctor);
        User user = userServiceImpl.findById(createAppointment.getUser()).orElseThrow();
        appointment.setUser(user);
        Hospital host = hospitalServiceImpl.findById(createAppointment.getHospitalId());
        if(host==null) return "böyle bir hastane  yok ";
        appointment.setHospitalId(host);
        appointment.setStatus(createAppointment.getStatus());
        appointment.setStartedDate(createAppointment.getStartedDate());
        appointment.setEndedDate(createAppointment.getEndedDate());
        appointmentRepository.save(appointment);
        return  "Created";
    }

    @Override
    public Optional<Appointment> findById(Long appointmetnId) {
        Optional<Appointment> appointment = appointmentRepository.findById(appointmetnId);
        if (appointment.isEmpty()) {
            throw new RuntimeException("Appointment not found");
        }
        return appointment;
    }

    @Override
    public List<Appointment> findByStartDate(LocalDateTime startDate) {
        List<Appointment> appointments = new ArrayList<Appointment>();
        return appointments;
    }

    @Override
    public List<Appointment> findByEndDate(LocalDateTime endDate) {
        List<Appointment> appointments = new ArrayList<Appointment>();
        return appointments;
    }

    @Override
    public void deleteAppointment(Long appointmetnId) {
        appointmentRepository.deleteById(appointmetnId);

    }
}
