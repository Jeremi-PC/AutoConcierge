package com.example.auto_concierge.service;

import com.example.auto_concierge.entity.Car;
import com.example.auto_concierge.entity.Role;
import com.example.auto_concierge.entity.User;
import com.example.auto_concierge.repository.CarRepository;
import com.example.auto_concierge.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Null;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class CarService {

    private final CarRepository carRepository;
    private final UserRepository userRepository;

    public CarService(CarRepository carRepository, UserRepository userRepository) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
    }

    public Car createCar(Long userId, Car car) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null && user.getRole() == Role.CLIENT) {
            if (carRepository.existsByVin(car.getVin())) {
                throw new RuntimeException("Машина с таким VIN уже существует");
            }
            car.setOwner(user);
            return carRepository.save(car);
        } else {
            throw new RuntimeException("Недостаточно прав для добавления машины или пользователь с указанным ID не найден");
        }
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }
    public Car getOneCar(Long carId) {
        return carRepository.findById(carId).orElse(null);
    }

    public Car getCarByUserIdAndCarId(Long userId, Long carId) {
        return carRepository.findByOwnerIdAndId(userId, carId);
    }

    public List<Car> getCarByUserId(Long userId) {
        return carRepository.findAllByOwnerId(userId);
    }

    public Car updateCar(Long userId, Long carId, Car carDetails) {
        User user = userRepository.findById(userId).orElse(null);
        Car car = carRepository.findByOwnerIdAndId(userId, carId);
        if (car != null || user != null) {
            car.setOwner(user);
            car.setBrand(carDetails.getBrand());
            car.setModel(carDetails.getModel());
            car.setVin(carDetails.getVin());
            car.setMileage(carDetails.getMileage());
            car.setYear(carDetails.getYear());
            car.setEngineType(carDetails.getEngineType());
            return carRepository.save(car);
        } else {
            throw new RuntimeException("Не возможно найти машину");
        }
    }


    public void deleteCar(Long userId, Long carId) {
        Car car = carRepository.findByOwnerIdAndId(userId, carId);
        if (car != null) {
            carRepository.deleteById(carId);
        } else
            throw new RuntimeException("Не возможно найти машину");
    }
}

