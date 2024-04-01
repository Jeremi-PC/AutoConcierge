package com.example.auto_concierge.entity.serviceCenter;

import jakarta.persistence.*;
import lombok.*;


@Getter
@Setter
@Embeddable
public class Address {

    private String street;
    private String city;
    private String state;
    private String postalCode;
    private Double latitude;
    private Double longitude;

}