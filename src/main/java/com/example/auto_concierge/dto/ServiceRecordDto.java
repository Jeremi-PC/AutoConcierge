package com.example.auto_concierge.dto;

import com.example.auto_concierge.entity.Service;
import com.example.auto_concierge.entity.ServiceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


import java.time.ZonedDateTime;
import java.util.List;


@AllArgsConstructor
@Getter
@Setter
public class ServiceRecordDto {
    private ServiceType serviceType;
    private ZonedDateTime appoitmentDateTime;
    private List<Service> services;
}
