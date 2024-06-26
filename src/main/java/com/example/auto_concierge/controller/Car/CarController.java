package com.example.auto_concierge.controller.Car;

import com.example.auto_concierge.dto.car.CarDTO;
import com.example.auto_concierge.entity.car.Car;
import com.example.auto_concierge.service.CarService;
import jakarta.annotation.security.RolesAllowed;
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
    @RolesAllowed(value = {"CLIENT","SERVICE_CENTER","ADMIN"})
    @PostMapping("/create")
    public Car createCar(@PathVariable Long userId, @RequestBody CarDTO carDTO) {
        return carService.createCar(userId, carDTO);
    }
    @RolesAllowed(value = {"CLIENT","SERVICE_CENTER","ADMIN"})
    @GetMapping
    public List<CarDTO> getCarsByUserId(@PathVariable Long userId) {
        return carService.getCarsByUserId(userId);
    }
}
