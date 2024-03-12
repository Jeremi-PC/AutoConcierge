package com.example.auto_concierge.entity;

import com.example.auto_concierge.entity.PartsOrder;
import com.example.auto_concierge.entity.Role;
import com.example.auto_concierge.entity.ServiceRecord;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "user")
    private Set<ServiceCenter> serviceCenters = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<ServiceRecord> serviceRecords = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<PartsOrder> partsOrders = new HashSet<>();


    public void addServiceRecord(ServiceRecord serviceRecord) {
        serviceRecords.add(serviceRecord);
        serviceRecord.setUser(this);
    }

    public void removeServiceRecord(ServiceRecord serviceRecord) {
        serviceRecords.remove(serviceRecord);
        serviceRecord.setUser(null);
    }

    public void addPartsOrder(PartsOrder partsOrder) {
        partsOrders.add(partsOrder);
        partsOrder.setUser(this);
    }

    public void removePartsOrder(PartsOrder partsOrder) {
        partsOrders.remove(partsOrder);
        partsOrder.setUser(null);
    }
}
