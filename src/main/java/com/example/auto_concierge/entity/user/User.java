package com.example.auto_concierge.entity.user;

import com.example.auto_concierge.entity.*;
import com.example.auto_concierge.entity.partsOrder.PartsOrder;
import com.example.auto_concierge.entity.serviceCenter.ServiceCenter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cascade;

import java.util.HashSet;
import java.util.List;
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
    private String username;
    private String password;
    private String email;
    @ElementCollection
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private List<Phone> phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "owner")
    private Set<ServiceCenter> serviceCenters = new HashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<PartsOrder> partsOrders = new HashSet<>();

    public void addPartsOrder(PartsOrder partsOrder) {
        partsOrders.add(partsOrder);
        partsOrder.setUser(this);
    }

    public void removePartsOrder(PartsOrder partsOrder) {
        partsOrders.remove(partsOrder);
        partsOrder.setUser(null);
    }
}
