package com.example.auto_concierge.dto.serviceCenter;

import com.example.auto_concierge.entity.Phone;
import com.example.auto_concierge.entity.serviceCenter.Address;
import com.example.auto_concierge.entity.serviceCenter.Schedule;

import java.util.List;

public record ServiceCenterDTO(
        Long id,
        String name,
        Address address,
        List<Phone> contactNumber,
        String website,
        float averageRating,
        List<Schedule> schedule,
        Long ownerId
) {}
