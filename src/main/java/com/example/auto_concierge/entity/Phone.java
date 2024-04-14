package com.example.auto_concierge.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Phone {
    private String countryCode;
    private String areaCode;
    private String number;
}

