package com.example.auto_concierge.service;

import com.example.auto_concierge.dto.car.CarDTO;
import com.example.auto_concierge.entity.car.Car;
import com.example.auto_concierge.entity.user.Role;
import com.example.auto_concierge.entity.user.User;
import com.example.auto_concierge.exception.DuplicateItemException;
import com.example.auto_concierge.exception.InsufficientPermissionException;
import com.example.auto_concierge.exception.NotFoundException;
import com.example.auto_concierge.mapper.CarMapper;
import com.example.auto_concierge.repository.CarRepository;
import com.example.auto_concierge.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
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
        return userRepository.findById(userId)
                .map(user -> {
                    if (user.getRole() == Role.CLIENT) {
                        Car car = carMapper.carDtoToCar(carDTO);
                        if (carRepository.existsByVin(car.getVin())) {
                            throw new DuplicateItemException("Машина с таким VIN уже существует");
                        }
                        car.setOwner(user);
                        return carRepository.save(car);
                    } else {
                        throw new InsufficientPermissionException("Недостаточно прав для добавления машины");
                    }
                })
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным ID не найден"));
    }



    public List<CarDTO> getAllCars() {
        List<Car> cars = carRepository.findAll();
        if (!cars.isEmpty()) {
            return cars.stream()
                    .map(CarMapper.INSTANCE::carToCarDto)
                    .collect(Collectors.toList());
        } else {
            throw new NotFoundException("Машины не найдены");
        }
    }

    public Car getCarById(Long carId) {
        return carRepository.findById(carId).orElseThrow(
                () -> new NotFoundException("Автомобиль с идентификатором " + carId + " не найден"));
    }

    public CarDTO getCarDTOById(Long carId) {
        return carRepository.findById(carId)
                .map(CarMapper.INSTANCE::carToCarDto)
                .orElseThrow(() -> new NotFoundException("Машина с идентификатором " + carId + " не найдена"));
    }

    public List<CarDTO> getCarByUserId(Long userId) {
        return userRepository.findById(userId)
                .map(user -> {
                    List<Car> cars = carRepository.findAllByOwnerId(userId);
                    if (!cars.isEmpty()) {
                        return cars.stream()
                                .map(CarMapper.INSTANCE::carToCarDto)
                                .collect(Collectors.toList());
                    } else {
                        throw new NotFoundException("Машины для пользователя с идентификатором " + userId + " не найдены");
                    }
                })
                .orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не найден"));
    }

    public CarDTO updateCar(Long carId, Car carDetails) {
        return carRepository.findById(carId)
                .map(car -> {
                    User user = car.getOwner();
                    if (user != null) {
                        car.setOwner(user);
                    } else {
                        throw new NotFoundException("Невозможно найти владельца машины");
                    }
                    car.setBrand(carDetails.getBrand());
                    car.setModel(carDetails.getModel());
                    car.setVin(carDetails.getVin());
                    car.setMileage(carDetails.getMileage());
                    car.setYearOfCreating(carDetails.getYearOfCreating());
                    car.setEngineType(carDetails.getEngineType());
                    Car updatedCar = carRepository.save(car);
                    return carMapper.carToCarDto(updatedCar);
                })
                .orElseThrow(() -> new NotFoundException("Машина с идентификатором " + carId + " не найдена"));
    }


    public void deleteCar(Long carId) {
        carRepository.findById(carId).ifPresentOrElse(
                car -> carRepository.deleteById(carId), () -> {
                    throw new NotFoundException("Машина с идентификатором " + carId + " не найдена");
                }
                );
    }

}

