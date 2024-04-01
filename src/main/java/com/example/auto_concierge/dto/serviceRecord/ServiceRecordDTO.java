package com.example.auto_concierge.dto.serviceRecord;

import com.example.auto_concierge.entity.serviceRecord.Service;
import com.example.auto_concierge.entity.serviceRecord.ServiceType;
import com.example.auto_concierge.entity.serviceRecord.Status;

import java.time.ZonedDateTime;
import java.util.List;

public record ServiceRecordDTO(
        Long carId,
        Long serviceCenterId,
        ServiceType serviceType,
        ZonedDateTime appointmentDateTime,
        List<Service> services,
        Status status
) {}
