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
@Table(name="hospitals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Hospital {

    @Id
    @UuidGenerator
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private UUID id;

    @Column(name="name")
    private String name;

    @Column(name="town")
    private String town;

    @Column(name="township")
    private String township;

    @Column(name="clinic")
    private String clinic;

    @OneToMany(mappedBy = "hospital")
    private Set<Doctor> doctors;

}
