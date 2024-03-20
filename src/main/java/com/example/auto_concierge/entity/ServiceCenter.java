package com.example.auto_concierge.entity;

import com.github.javafaker.PhoneNumber;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table (name = "service_centers")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ServiceCenter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @Embedded
    private Address address;
    @ElementCollection
    private List<Phone> contactNumber;
    private String website;
    private float averageRating;
    @ElementCollection
    private List<Schedule> schedule;

    @ManyToOne
    private User owner;

    @OneToMany(mappedBy = "serviceCenter")
    private List<ServiceRecord> serviceRecords;


}
