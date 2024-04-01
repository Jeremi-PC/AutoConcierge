package com.example.auto_concierge.controller;

import com.example.auto_concierge.entity.car.Car;
import com.example.auto_concierge.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users/{userId}/cars")
public class CarController {

    private final CarService carService;

    @Autowired
    public CarController(CarService carService) {
        this.carService = carService;
    }

    @PostMapping("/create")
    public Car createCar(@PathVariable Long userId, @RequestBody Car car) {
        return carService.createCar(userId, car);
    }

    @GetMapping
    public List<Car> geCarsByUserId(@PathVariable Long userId) {
        return carService.getCarByUserId(userId);
    }

    @GetMapping("/{carId}")
    public Car getCarByUserIdAndCarId(@PathVariable Long userId, @PathVariable Long carId) {
        return carService.getCarByUserIdAndCarId(userId, carId);
    }

    @PutMapping("/{carId}")
    public Car updateCar(@PathVariable Long userId, @PathVariable Long carId, @RequestBody Car carDetails) {
        return carService.updateCar(userId, carId, carDetails);
    }

    @DeleteMapping("/{carId}")
    public void deleteCar(@PathVariable Long userId, @PathVariable Long carId) {
        carService.deleteCar(userId, carId);
    }
}
