package com.example.auto_concierge.entity.serviceRecord;

import com.example.auto_concierge.entity.car.Car;
import com.example.auto_concierge.entity.serviceCenter.ServiceCenter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
    @NotNull
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id")
    private Car car;
    @NotNull
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "service_center_id")
    private ServiceCenter serviceCenter;
    @NotNull
    @Enumerated(EnumType.STRING)
    private ServiceType serviceType;
    @Future
    private ZonedDateTime appointmentDateTime;
    private ZonedDateTime creatingTime;
    @Size(min = 1)
    @ElementCollection
    private List<Service> services;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;


    public ServiceRecord(Car car, ServiceCenter serviceCenter, ServiceType serviceType, ZonedDateTime appointmentDateTime, List<Service> services, ZonedDateTime creatingTime) {
        this.car = car;
        this.serviceCenter = serviceCenter;
        this.serviceType = serviceType;
        this.appointmentDateTime = appointmentDateTime;
        this.services = services;
        this.status = Status.CREATED;
        this.creatingTime = ZonedDateTime.now();
    }

}
