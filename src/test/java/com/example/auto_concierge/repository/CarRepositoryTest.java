package com.example.auto_concierge.repository;

import com.example.auto_concierge.entity.Phone;
import com.example.auto_concierge.entity.car.Car;
import com.example.auto_concierge.entity.user.User;
import com.example.auto_concierge.entity.user.Role;
import com.example.auto_concierge.service.CarService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNull;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Transactional
class CarRepositoryTest {

    @Autowired
    private CarRepository carRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CarService carService;
    public static User createUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setUsername("john.doe");
        user.setPassword("password123");

        Phone phone1 = new Phone("+1", "123", "456789");
        Phone phone2 = new Phone("+1", "987", "654321");
        List<Phone> phoneNumbers = List.of(phone1, phone2);
        user.setPhoneNumber(phoneNumbers);

        user.setRole(Role.CLIENT);
        return user;
    }

    @BeforeEach
    void setUp() {
        userRepository.save(createUser());
        carRepository.deleteAll();
    }
    @Test
    void save(){
    User user = userRepository.findById(1L).orElse(null);

    Car car = new Car();
        car.setOwner(user);
        car.setBrand("Toyota");
        car.setModel("Camry");
        car.setVin("123456789");
        car.setYearOfCreating(LocalDate.parse("2020-01-01"));
        car.setLicensePlate("ABC123");
        car.setMileage(50000);
        car.setEngineType("Gasoline");

        carRepository.save(car);

    assertNotNull(car.getId());
    assertEquals(user, car.getOwner());
}
    @Test
    void saveCarWithIncompleteData() {
        User user = userRepository.findById(1L).orElse(null);
        Car car = new Car();
        car.setOwner(user);
        car.setBrand("Toyota");

        assertThrows(ConstraintViolationException.class, () -> carRepository.save(car));

    }
    @Test
    void findCarById() {
        User user = userRepository.findById(1L).orElse(null);
        Car car = new Car();
        car.setOwner(user);
        car.setBrand("Toyota");
        car.setModel("Camry");
        car.setYearOfCreating(LocalDate.of(2020, 1, 1));
        car.setLicensePlate("ABC123");
        car.setMileage(10000);
        car.setVin("123456789");
        car.setEngineType("Gasoline");
        carRepository.save(car);

        Optional<Car> optionalCar = carRepository.findById(car.getId());

        assertTrue(optionalCar.isPresent());
        assertEquals(car, optionalCar.get());
    }
    @Test
    void findAllCars() {
        User user = userRepository.findById(1L).orElse(null);
        Car car1 = new Car(1L, user, "Toyota", "Camry", LocalDate.of(2020, 1, 1), "ABC123", 10000, "123456789", "Gasoline");
        Car car2 = new Car(2L, user, "Honda", "Accord", LocalDate.of(2019, 1, 1), "DEF456", 20000, "987654321", "Gasoline");
        carRepository.saveAll(List.of(car1, car2));
        List<Car> allCars = carRepository.findAll();
        assertFalse(allCars.isEmpty());
        assertEquals(2, allCars.size());
        assertEquals(1L, allCars.get(0).getId());
        assertEquals(2L, allCars.get(1).getId());
    }
    @Test
    void deleteCar() {
        User user = userRepository.findById(1L).orElse(null);
        Car car = new Car(1L, user, "Toyota", "Camry", LocalDate.of(2020, 1, 1), "ABC123", 10000, "123456789", "Gasoline");
        carRepository.save(car);

        assertTrue(carRepository.existsById(car.getId()));
        carRepository.deleteById(car.getId());

        assertFalse(carRepository.existsById(car.getId()));
    }
    @Test
    void updateCar() {
        User user = userRepository.findById(1L).orElse(null);
        Car car = new Car(1L, user, "Toyota", "Camry", LocalDate.of(2020, 1, 1), "ABC123", 10000, "123456789", "Gasoline");
        carRepository.save(car);

        car.setBrand("Honda");
        car.setModel("Accord");
        car.setYearOfCreating(LocalDate.of(2019, 1, 1));
        car.setLicensePlate("XYZ987");
        car.setMileage(20000);
        car.setVin("987654321");
        car.setEngineType("Diesel");

        carRepository.save(car);

        Car updatedCar = carRepository.findById(car.getId()).orElse(null);

        assertNotNull(updatedCar);
        assertEquals("Honda", updatedCar.getBrand());
        assertEquals("Accord", updatedCar.getModel());
        assertEquals(LocalDate.of(2019, 1, 1), updatedCar.getYearOfCreating());
        assertEquals("XYZ987", updatedCar.getLicensePlate());
        assertEquals(20000, updatedCar.getMileage());
        assertEquals("987654321", updatedCar.getVin());
        assertEquals("Diesel", updatedCar.getEngineType());
    }


    @Test
    void findByOwnerIdAndIdExistingCarReturnsCar() {
        User user = userRepository.findById(1L).orElse(null);
        assertNotNull(user);

        Car car = new Car(1L, user, "Toyota", "Camry", LocalDate.of(2020, 1, 1), "ABC123", 10000, "123456789", "Gasoline");
        car.setOwner(user);
        carRepository.save(car);

        Optional<Car> foundCar = Optional.ofNullable(carRepository.findByOwnerIdAndId(user.getId(), car.getId()));
        assertTrue(foundCar.isPresent());
        assertEquals(car, foundCar.get());
    }

    @Test
    void findByOwnerIdAndIdNonExistingCarReturnsEmptyOptional() {
        User user = userRepository.findById(1L).orElse(null);
        Optional<Car> foundCar = Optional.ofNullable(carRepository.findByOwnerIdAndId(user.getId(), 100L));
        assertTrue(foundCar.isEmpty());
    }

    @Test
    void findAllByOwnerIdExistingCarsReturnsListOfCars() {
        User user = userRepository.findById(1L).orElse(null);

        Car car1 = new Car(1L, user, "Toyota", "Camry", LocalDate.of(2020, 1, 1), "ABC123", 10000, "123456789", "Gasoline");
        Car car2 = new Car(2L, user, "Honda", "Accord", LocalDate.of(2019, 1, 1), "DEF456", 20000, "987654321", "Gasoline");

        carRepository.saveAll(List.of(car1, car2));
        assert user != null;
        List<Car> cars = carRepository.findAllByOwnerId(user.getId());
        assertFalse(cars.isEmpty());
        assertEquals(2, cars.size());
        for (Car car : cars) {
            assertEquals(user.getId(), car.getOwner().getId());
        }
    }
    @Test
    void findAllByOwnerIdNoCarsReturnsEmptyList() {
        User user = userRepository.findById(1L).orElse(null);
        assert user != null;
        List<Car> cars = carRepository.findAllByOwnerId(user.getId());
        assertTrue(cars.isEmpty());
    }


    @Test
    void saveCarWithDuplicateVin() {
        User user = userRepository.findById(1L).orElse(null);
        assertNotNull(user);
        Car car1 = new Car(1L, user, "Toyota", "Camry", LocalDate.of(2020, 1, 1), "ABC123", 10000, "123456789", "Gasoline");
        Car car2 = new Car(2L, user, "Honda", "Accord", LocalDate.of(2019, 1, 1), "ABC123", 20000, "987654321", "Gasoline");

        carRepository.save(car1);
        assertThrows(RuntimeException.class, () -> carService.createCar(user.getId(), car2));
    }
}