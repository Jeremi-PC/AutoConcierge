package com.example.auto_concierge.dto;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class ServiceRecordDto {

    @Data
    public class AppointmentDto {
        private Long serviceCenterId;
        private LocalDateTime dateTime;
        private List<String> services;
        // Другие необходимые поля
    }
}
