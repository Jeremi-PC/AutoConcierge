package com.example.auto_concierge.controller.Car;

import com.example.auto_concierge.dto.car.CarDTO;
import com.example.auto_concierge.entity.car.Car;
import com.example.auto_concierge.service.CarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class GeneralCarRestController {
    private final CarService carService;

    @Autowired
    public GeneralCarRestController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping()
    public List<CarDTO> getAllCars() {
        return carService.getAllCars();
    }
    @GetMapping("/{carId}")
    public CarDTO getCarById(@PathVariable Long carId) {
        return carService.getCarDTOById(carId);
    }
    @PutMapping("/{carId}")
    public CarDTO updateCar(@PathVariable Long carId, @RequestBody Car carDetails) {
        return carService.updateCar(carId, carDetails);
    }
    @DeleteMapping("/{carId}")
    public void deleteCar(@PathVariable Long carId) {
        carService.deleteCar(carId);
    }

}