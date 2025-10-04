package com.aysekoc.hospitalappointmantsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Entity
@Table(name="doctors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="doctor_id")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="surname")
    private String surname;

    @Column(name="password")
    private String password;

    @Column(name="username")
    private String username;

    @Enumerated(EnumType.STRING)
    private Specialty specialty;

    @Column(name="title")
    private String title;

    @Column(name="gender")
    private boolean gender;

    @Column(name="age")
    private int age;

    @OneToMany(mappedBy = "doctor")
    private Set<Appointment> appointments;

    @ManyToOne
    @JoinColumn(name="hospital_id")
    private Hospital hospital;

    @ManyToMany(mappedBy = "doctors")
    private Set<User> users;

    @Column(name="appointment_id")
    private Long appointmentId;

    @Column(name="role")
    @Enumerated(EnumType.STRING)
    private Roles role;




}
