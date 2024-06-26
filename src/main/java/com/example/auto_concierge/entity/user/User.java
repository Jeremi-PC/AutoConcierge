package com.example.auto_concierge.entity.user;

import com.example.auto_concierge.entity.*;
import com.example.auto_concierge.entity.car.Car;
import com.example.auto_concierge.entity.partsOrder.PartsOrder;
import com.example.auto_concierge.entity.serviceCenter.ServiceCenter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "First name must not be blank")
    private String firstName;
    @NotBlank(message = "Last name must not be blank")
    private String lastName;
    @NotBlank(message = "Username must not be blank")
    private String username;
    @NotBlank(message = "Password must not be blank")
    private String password;
    @NotBlank(message = "E-mail must not be blank")
    @Email
    private String email;
    @ElementCollection
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    @Size(min = 1)
    private List<Phone> phoneNumber;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Car> cars = new HashSet<>();
    
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ServiceCenter> serviceCenters = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
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
