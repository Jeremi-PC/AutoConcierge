package com.example.auto_concierge.controller.Car;

import com.example.auto_concierge.dto.car.CarDTO;
import com.example.auto_concierge.entity.car.Car;
import com.example.auto_concierge.service.CarService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class GeneralCarController {
    private final CarService carService;

    @Autowired
    public GeneralCarController(CarService carService) {
        this.carService = carService;
    }
    @RolesAllowed(value = {"CLIENT","SERVICE_CENTER","ADMIN"})
    @GetMapping()
    public List<CarDTO> getAllCars() {
        return carService.getAllCars();
    }
    @RolesAllowed(value = {"CLIENT","SERVICE_CENTER","ADMIN"})
    @GetMapping("/{carId}")
    public CarDTO getCarById(@PathVariable Long carId) {
        return carService.getCarDTOById(carId);
    }
    @RolesAllowed(value = {"CLIENT","SERVICE_CENTER","ADMIN"})
    @PutMapping("/{carId}")
    public CarDTO updateCar(@PathVariable Long carId, @RequestBody Car carDetails) {
        return carService.updateCar(carId, carDetails);
    }
    @DeleteMapping("/{carId}")
    @RolesAllowed(value = {"CLIENT","SERVICE_CENTER","ADMIN"})
    public void deleteCar(@PathVariable Long carId) {
        carService.deleteCar(carId);
    }

}
