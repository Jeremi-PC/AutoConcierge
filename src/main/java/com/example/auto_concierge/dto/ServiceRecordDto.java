package com.example.auto_concierge.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ServiceRecordDto {
    private LocalDateTime dateTime;
    private List<String> services;

    public ServiceRecordDto(Long serviceCenterId, LocalDateTime dateTime, List<String> services) {
        this.serviceCenterId = serviceCenterId;
        this.dateTime = dateTime;
        this.services = services;
    }
}
