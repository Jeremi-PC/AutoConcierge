package com.example.auto_concierge.repository;

import com.example.auto_concierge.entity.car.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    boolean existsByVin(String vin);
    List<Car> findAllByOwnerId(Long userId);

}
