package com.aysekoc.hospitalappointmantsystem.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Entity
@Table(name="hospitals")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="hospital_id")
    private Long id;

    @Column(name="name")
    private String name;

    @Column(name="town")
    private String town;

    @Column(name="township")
    private String township;

    @Column(name="clinic")
    private String clinic;

    @OneToMany(mappedBy = "hospital")
    private List<Doctor> doctors;

}
