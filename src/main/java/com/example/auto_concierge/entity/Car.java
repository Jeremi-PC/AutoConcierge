package com.example.auto_concierge.entity;



import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table (name = "cars")
@NoArgsConstructor
@AllArgsConstructor
@Data

public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    private String brand;
    private String model;
    @Past
    private LocalDate year;
    private String licensePlate;
    private Integer mileage;
    private String vin;
    private String engineType;



    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
