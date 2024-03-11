package com.example.auto_concierge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "parts_orders")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PartsOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    private String partName;

    private Integer quantity;

    private String deliveryAddress;

    private LocalDateTime orderDateTime;

    @Enumerated(EnumType.STRING)
    private Status status;

}
