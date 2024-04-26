package com.example.auto_concierge.dto.car;

import java.time.LocalDate;

public record CarDTO(
        Long id,
        String brand,
        String model,
        LocalDate yearOfCreating,
        String licensePlate,
        Integer mileage,
        String vin,
        String engineType,
        Long owner
) {}


