package com.example.auto_concierge.entity.car;



import com.example.auto_concierge.entity.serviceRecord.ServiceRecord;
import com.example.auto_concierge.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User owner;

    private String brand;
    private String model;
    @Past
  //  @Column(name = "year", columnDefinition = "int")
    private LocalDate year;
    private String licensePlate;
    private Integer mileage;
    private String vin;
    private String engineType;
    @OneToMany(mappedBy = "car")
    @JsonIgnore
    private List<ServiceRecord> serviceRecords;



    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
