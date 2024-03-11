package com.example.auto_concierge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table (name = "service_records")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ServiceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Car car;

    @ManyToOne
    private ServiceCenter serviceCenter;

    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;

    private LocalDateTime appointmentDateTime;

    @Enumerated(EnumType.STRING)
    private Status status;

}
