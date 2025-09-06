package com.aysekoc.hospitalappointmantsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name="prescriptions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Prescription {

    @Id
    @UuidGenerator
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

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
