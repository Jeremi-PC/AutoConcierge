package com.example.auto_concierge.service;

import com.example.auto_concierge.entity.car.Car;
import com.example.auto_concierge.entity.user.Role;
import com.example.auto_concierge.entity.user.User;
import com.example.auto_concierge.repository.CarRepository;
import com.example.auto_concierge.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public Car getCarById(Long carId) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        if (optionalCar.isPresent()) {
            return optionalCar.get();
        } else {
            throw new IllegalArgumentException("Автомобиль с идентификатором " + carId + " не найден");
        }
    }

    public Car getCarByUserIdAndCarId(Long userId, Long carId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь с идентификатором " + userId + " не найден");
        }
        Car car = carRepository.findByOwnerIdAndId(userId, carId);
        if (car == null) {
            throw new IllegalArgumentException("Автомобиль с идентификатором " + carId + " не найден для пользователя с идентификатором " + userId);
        }
        return car;
    }

    public List<Car> getCarByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь с идентификатором " + userId + " не найден");
        }
        List<Car> cars = carRepository.findAllByOwnerId(userId);
        if (cars.isEmpty()) {
            throw new IllegalArgumentException("Пользователь с идентификатором " + userId + " не имеет ни одного автомобиля");
        }
        return cars;

    }

    public Car updateCar(Long userId, Long carId, Car carDetails) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new IllegalArgumentException("Пользователь с идентификатором " + userId + " не найден");
        }
        Car car = carRepository.findByOwnerIdAndId(userId, carId);
        if (car == null) {
            throw new IllegalArgumentException("Автомобиль с идентификатором " + carId + " не найден для пользователя с идентификатором " + userId);
        }
        car.setBrand(carDetails.getBrand());
        car.setModel(carDetails.getModel());
        car.setVin(carDetails.getVin());
        car.setMileage(carDetails.getMileage());
        car.setYearOfCreating(carDetails.getYearOfCreating());
        car.setEngineType(carDetails.getEngineType());

        return carRepository.save(car);
    }


    public void deleteCar(Long userId, Long carId) {
        Car car = carRepository.findByOwnerIdAndId(userId, carId);
        if (car != null) {
            carRepository.deleteById(carId);
        } else
            throw new RuntimeException("Не возможно найти машину");
    }
}

