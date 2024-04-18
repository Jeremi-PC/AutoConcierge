package com.example.auto_concierge.entity.serviceCenter;

import com.example.auto_concierge.entity.Phone;
import com.example.auto_concierge.entity.serviceRecord.ServiceRecord;
import com.example.auto_concierge.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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

    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User owner;

    @OneToMany(mappedBy = "serviceCenter")
    @JsonIgnore
    private List<ServiceRecord> serviceRecords;

}
