package com.example.auto_concierge.repository;

import com.example.auto_concierge.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    boolean existsByVin(String vin);
    List<Car> findAllByOwnerId(Long userId);
    Car findByOwnerIdAndId(Long userId, Long carId);

}
