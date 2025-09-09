package com.aysekoc.hospitalappointmantsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="prescriptions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="prescription_id")
    private Long id;

    @Column(name="medicine_name")
    private String medicineName;

    @Column(name="diagnosis")
    private String diagnosis;

    @Column(name="date")
    private LocalDateTime date;

    @Column(name="hash_prescription")
    private String hashPrescription;


    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;



}