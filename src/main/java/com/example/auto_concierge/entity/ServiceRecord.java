package com.example.auto_concierge.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table (name = "service_records")
@NoArgsConstructor
@Data
public class ServiceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Car car;

    @ManyToOne
    private ServiceCenter serviceCenter;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    private ZonedDateTime appointmentDateTime;

    @ElementCollection
    private List<Service> services;

    @Enumerated(EnumType.STRING)
    private Status status;

    public ServiceRecord(Car car, ServiceCenter serviceCenter, ServiceType serviceType, ZonedDateTime appointmentDateTime, List<Service> services) {
        this.car = car;
        this.serviceCenter = serviceCenter;
        this.serviceType = serviceType;
        this.appointmentDateTime = appointmentDateTime;
        this.services = services;
        this.status = Status.CREATED;
    }

}
