package com.example.auto_concierge.repository;

import com.example.auto_concierge.entity.Phone;
import com.example.auto_concierge.entity.car.Car;
import com.example.auto_concierge.entity.serviceCenter.Address;
import com.example.auto_concierge.entity.serviceCenter.ServiceCenter;
import com.example.auto_concierge.entity.serviceRecord.ServiceRecord;
import com.example.auto_concierge.entity.serviceRecord.ServiceType;
import com.example.auto_concierge.entity.user.Role;
import com.example.auto_concierge.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ServiceRecordRepositoryTest {
    @Autowired
    private ServiceRecordRepository serviceRecordRepository;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private ServiceCenterRepository serviceCenterRepository;

    @Autowired
    private UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;
    private User createUserClient() {
        User userClient = new User();
        userClient.setFirstName("John");
        userClient.setLastName("Doe");
        userClient.setEmail("john.doe@example.com");
        userClient.setUsername("john.doe");
        userClient.setPassword("password123");
        userClient.setRole(Role.CLIENT);
        return userClient;
    }
    private User createUserServiceCenter() {
        User userServiceCenter = new User();
        userServiceCenter.setFirstName("Jack");
        userServiceCenter.setLastName("Brown");
        userServiceCenter.setEmail("jack.brown@example.com");
        userServiceCenter.setUsername("jack.brown");
        userServiceCenter.setPassword("password456");
        userServiceCenter.setRole(Role.SERVICE_CENTER);
        return userServiceCenter;
    }
    private Car createCar(User user) {
        Car car = new Car();
        car.setOwner(user);
        car.setBrand("Honda");
        car.setModel("Accord");
        car.setYearOfCreating(LocalDate.of(2019, 1, 1));
        car.setLicensePlate("XYZ987");
        car.setMileage(20000);
        car.setVin("987654321");
        car.setEngineType("Diesel");
        return car;
    }
    private ServiceCenter createServiceCenter(User user){
        ServiceCenter serviceCenter = new ServiceCenter();
        serviceCenter.setOwner(user);
        serviceCenter.setName("Example Service Center");
        serviceCenter.setAddress(createAddress());
        serviceCenter.setContactNumber(createContactNumbers());
        serviceCenter.setWebsite("http://example.com");
        serviceCenter.setAverageRating(4.5f);
        return serviceCenter;
    }
    private Address createAddress() {
        Address address = new Address();
        address.setStreet("123 Main St");
        address.setCity("Anytown");
        address.setState("Anystate");
        address.setPostalCode("12345");
        address.setLatitude(40.7128);
        address.setLongitude(-74.0060);
        return address;
    }
    private List<Phone> createContactNumbers() {
        Phone contactNumber1 = new Phone("+1", "123", "456789");
        Phone contactNumber2 = new Phone("+1", "987", "654321");
        return List.of(contactNumber1, contactNumber2);
    }
    private ServiceRecord createServiceRecord(Car car, ServiceCenter serviceCenter) {
        return new ServiceRecord(
                car,
                serviceCenter,
                ServiceType.MAINTENANCE,
                ZonedDateTime.parse("2024-12-03T10:15:30+01:00"),
                null,
                ZonedDateTime.now());
    }
    @BeforeEach
    void setUp() {
        serviceRecordRepository.deleteAll();
    }
    @Test
    void saveServiceRecord() {
        User userCl = createUserClient();
        User userSC = createUserServiceCenter();
        userRepository.save(userCl);
        userRepository.save(userSC);
        Car car = createCar(userCl);
        carRepository.save(car);
        ServiceCenter serviceCenter = createServiceCenter(userSC);
        serviceCenterRepository.save(serviceCenter);
        ServiceRecord serviceRecord = createServiceRecord(car, serviceCenter);
        serviceRecordRepository.save(serviceRecord);

        assertNotNull(serviceRecord.getId());
        }
    @Test
    void findServiceRecordById() {
        User userCl = createUserClient();
        User userSC = createUserServiceCenter();
        userRepository.save(userCl);
        userRepository.save(userSC);
        Car car = createCar(userCl);
        carRepository.save(car);
        ServiceCenter serviceCenter = createServiceCenter(userSC);
        serviceCenterRepository.save(serviceCenter);
        ServiceRecord serviceRecord = createServiceRecord(car, serviceCenter);
        serviceRecordRepository.save(serviceRecord);

        Optional<ServiceRecord> optionalServiceRecord = serviceRecordRepository.findById(serviceRecord.getId());

        assertTrue(optionalServiceRecord.isPresent());
        assertEquals(serviceRecord, optionalServiceRecord.get());
    }
    @Test
    void findAllServiceRecords() {
        User userCl = createUserClient();
        User userSC = createUserServiceCenter();
        userRepository.save(userCl);
        userRepository.save(userSC);
        Car car = createCar(userCl);
        carRepository.save(car);
        ServiceCenter serviceCenter = createServiceCenter(userSC);
        serviceCenterRepository.save(serviceCenter);
        ServiceRecord serviceRecord1 = createServiceRecord(car, serviceCenter);
        ServiceRecord serviceRecord2 = createServiceRecord(car, serviceCenter);
        serviceRecordRepository.saveAll(List.of(serviceRecord1, serviceRecord2));

        List<ServiceRecord> allServiceRecords = serviceRecordRepository.findAll();
        assertFalse(allServiceRecords.isEmpty());
        assertEquals(2, allServiceRecords.size());
        assertTrue(allServiceRecords.contains(serviceRecord1));
        assertTrue(allServiceRecords.contains(serviceRecord2));
    }
    @Test
    void deleteServiceRecordById() {
        User userCl = createUserClient();
        User userSC = createUserServiceCenter();
        userRepository.save(userCl);
        userRepository.save(userSC);
        Car car = createCar(userCl);
        carRepository.save(car);
        ServiceCenter serviceCenter = createServiceCenter(userSC);
        serviceCenterRepository.save(serviceCenter);
        ServiceRecord serviceRecord = createServiceRecord(car, serviceCenter);
        serviceRecordRepository.save(serviceRecord);

        serviceRecordRepository.deleteById(serviceRecord.getId());

        assertFalse(serviceRecordRepository.existsById(serviceRecord.getId()));
    }
    @Test
    void updateServiceRecord() {
        User userCl = createUserClient();
        User userSC = createUserServiceCenter();
        userRepository.save(userCl);
        userRepository.save(userSC);
        Car car = createCar(userCl);
        carRepository.save(car);
        ServiceCenter serviceCenter = createServiceCenter(userSC);
        serviceCenterRepository.save(serviceCenter);
        ServiceRecord serviceRecord = createServiceRecord(car, serviceCenter);
        serviceRecordRepository.save(serviceRecord);

        ServiceRecord updatedServiceRecord = serviceRecordRepository.findById(serviceRecord.getId()).orElse(null);
        assertNotNull(updatedServiceRecord);

        updatedServiceRecord.setServiceType(ServiceType.REPAIR);

        ServiceRecord savedServiceRecord = serviceRecordRepository.save(updatedServiceRecord);

        assertEquals(ServiceType.REPAIR, savedServiceRecord.getServiceType());
    }
    @Test
    void saveServiceRecordWithIncompleteData() {
        User userCl = createUserClient();
        User userSC = createUserServiceCenter();
        userRepository.save(userCl);
        userRepository.save(userSC);
        Car car = createCar(userCl);
        carRepository.save(car);
        ServiceCenter serviceCenter = createServiceCenter(userSC);
        serviceCenterRepository.save(serviceCenter);
        ServiceRecord serviceRecord = new ServiceRecord(
                car,
                serviceCenter,
                null,
                null,
                null,
                null);


        assertThrows(Exception.class, () -> {
            serviceRecordRepository.save(serviceRecord);
        });
    }




}




