package com.aysekoc.hospitalappointmantsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name="appointments")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="appointment_id")
    private Long id;

    @Column(name="started_date")
    private LocalDateTime startedDate;

    @Column(name="ended_date")
    private LocalDateTime endedDate;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name="doctor_id")
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name="hospital_id")
    private Hospital hospitalId;

    @OneToOne(mappedBy = "appointment", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Prescription prescription;









}
