package com.example.auto_concierge.entity.car;



import com.example.auto_concierge.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
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

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;
    @NotBlank(message = "Brand name must not be blank")
    private String brand;
    @NotBlank(message = "Model name must not be blank")
    private String model;
    @Past
    @NotNull
    private LocalDate yearOfCreating;
    @NotBlank(message = "License plate name must not be blank")
    private String licensePlate;
    @NotNull
    @Positive
    private Integer mileage;
    @NotBlank(message = "VIN must not be blank")
    private String vin;
    @NotBlank(message = "Engine type must not be blank")
    private String engineType;



    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
