package com.example.auto_concierge.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Embeddable
@Getter
@Setter
public class Phone {
    private String countryCode;
    private String areaCode;
    private String number;
}

