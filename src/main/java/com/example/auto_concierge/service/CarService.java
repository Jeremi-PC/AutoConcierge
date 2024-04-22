package com.example.auto_concierge.service;

import com.example.auto_concierge.dto.car.CarDTO;
import com.example.auto_concierge.entity.car.Car;
import com.example.auto_concierge.entity.user.Role;
import com.example.auto_concierge.entity.user.User;
import com.example.auto_concierge.mapper.CarMapper;
import com.example.auto_concierge.repository.CarRepository;
import com.example.auto_concierge.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@Transactional
public class CarService {

    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final CarMapper carMapper;

    public CarService(CarRepository carRepository, UserRepository userRepository, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.userRepository = userRepository;
        this.carMapper = carMapper;
    }

    public Car createCar(Long userId, CarDTO carDTO) {
        User user = userRepository.findById(userId).orElse(null);
        Car car = carMapper.carDtoToCar(carDTO);
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

    public List<CarDTO> getAllCars() {
        List<Car> cars = carRepository.findAll();
        return cars.stream()
                .map(CarMapper.INSTANCE::carToCarDto)
                .collect(Collectors.toList());
    }

    public Car getCarById(Long carId) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        if (optionalCar.isPresent()) {
            return optionalCar.get();
        } else {
            throw new IllegalArgumentException("Автомобиль с идентификатором " + carId + " не найден");
        }
    }

    public CarDTO getCarDTOById(Long carId) {
        Car car = carRepository.findById(carId).orElse(null);
        if (car != null) {
            return CarMapper.INSTANCE.carToCarDto(car);
        } else {
            return null;
        }
    }

    public List<CarDTO> getCarByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            List<Car> cars = carRepository.findAllByOwnerId(userId);
            return cars.stream()
                    .map(CarMapper.INSTANCE::carToCarDto)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    public CarDTO updateCar(Long carId, Car carDetails) {
        Car car = carRepository.findById(carId).orElse(null);
        if (car != null) {
            User user = car.getOwner();
            if (user != null) {
                car.setOwner(user);
            } else {
                throw new RuntimeException("Не возможно найти владельца машины");
            }
            car.setBrand(carDetails.getBrand());
            car.setModel(carDetails.getModel());
            car.setVin(carDetails.getVin());
            car.setMileage(carDetails.getMileage());
            car.setYearOfCreating(carDetails.getYearOfCreating());
            car.setEngineType(carDetails.getEngineType());
            Car updatedCar = carRepository.save(car);
            return carMapper.carToCarDto(updatedCar);
        } else {
            throw new RuntimeException("Невозможно найти машину");
        }
    }

    public void deleteCar(Long carId) {
        Car car = carRepository.findById(carId).orElse(null);;
        if (car != null) {
            carRepository.deleteById(carId);
        } else
            throw new RuntimeException("Невозможно найти машину");
    }
}

