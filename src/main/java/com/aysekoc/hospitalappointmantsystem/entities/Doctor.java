package com.aysekoc.hospitalappointmantsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name="doctors")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Doctor {
    @Id
    @UuidGenerator
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name="name")
    private String name;

    @Column(name="surname")
    private String surname;

    @Column(name="specialty")
    private String specialty;

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





}
