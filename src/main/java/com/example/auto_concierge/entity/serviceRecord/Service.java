package com.example.auto_concierge.entity.serviceRecord;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    private String name;
    private double price;

}
