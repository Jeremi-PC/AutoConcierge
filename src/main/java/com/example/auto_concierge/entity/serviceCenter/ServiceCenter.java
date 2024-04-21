package com.example.auto_concierge.entity.serviceCenter;

import com.example.auto_concierge.entity.Phone;
import com.example.auto_concierge.entity.serviceRecord.ServiceRecord;
import com.example.auto_concierge.entity.user.User;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

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
    @NotBlank
    private String name;
    @Embedded
    @Valid
    private Address address;
    @ElementCollection
    @Size(min = 1)
    private List<Phone> contactNumber;
    @URL
    private String website;
    private float averageRating;
    @ElementCollection
    private List<Schedule> schedule;
    @NotNull
    @ManyToOne (fetch = FetchType.LAZY)
    private User owner;
    @OneToMany(mappedBy = "serviceCenter")
    private List<ServiceRecord> serviceRecords;
}
