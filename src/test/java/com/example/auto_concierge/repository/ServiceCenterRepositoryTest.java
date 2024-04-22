package com.example.auto_concierge.repository;

import com.example.auto_concierge.entity.Phone;
import com.example.auto_concierge.entity.serviceCenter.Address;
import com.example.auto_concierge.entity.serviceCenter.ServiceCenter;
import com.example.auto_concierge.entity.user.Role;
import com.example.auto_concierge.entity.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class ServiceCenterRepositoryTest {
    @Autowired
    private ServiceCenterRepository serviceCenterRepository;
    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;

    public static User createUser() {
        User user = new User();
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john.doe@example.com");
        user.setUsername("john.doe");
        user.setPassword("password123");
        user.setRole(Role.SERVICE_CENTER);
        return user;
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

    @BeforeEach
    void setUp() {
        entityManager.createNativeQuery("ALTER TABLE service_centers ALTER COLUMN id RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE users ALTER COLUMN id RESTART WITH 1").executeUpdate();
        userRepository.save(createUser());
        serviceCenterRepository.deleteAll();
    }
    @Test
    void saveServiceCenter() {
        User user = userRepository.findById(1L).orElse(null);

        ServiceCenter serviceCenter = createServiceCenter(user);

        serviceCenterRepository.save(serviceCenter);

        assertNotNull(serviceCenter.getId());
        assertEquals(user, serviceCenter.getOwner());
    }
    @Test
    void findServiceCenterById() {
        User user = userRepository.findById(1L).orElse(null);
        assertNotNull(user);

        ServiceCenter serviceCenter = createServiceCenter(user);
        serviceCenterRepository.save(serviceCenter);

        Optional<ServiceCenter> optionalServiceCenter = serviceCenterRepository.findById(serviceCenter.getId());
        assertTrue(optionalServiceCenter.isPresent());
        assertEquals(serviceCenter, optionalServiceCenter.get());
    }
    @Test
    void findAllServiceCenters() {
        User user = userRepository.findById(1L).orElse(null);
        assertNotNull(user);

        ServiceCenter serviceCenter1 = createServiceCenter(user);
        ServiceCenter serviceCenter2 = createServiceCenter(user);

        serviceCenterRepository.saveAll(List.of(serviceCenter1, serviceCenter2));

        List<ServiceCenter> allServiceCenters = serviceCenterRepository.findAll();
        assertFalse(allServiceCenters.isEmpty());
        assertEquals(2, allServiceCenters.size());
        assertEquals(1L, allServiceCenters.get(0).getId());
        assertEquals(2L, allServiceCenters.get(1).getId());
    }
    @Test
    void updateServiceCenter() {
        User user = userRepository.findById(1L).orElse(null);
        assertNotNull(user);

        ServiceCenter serviceCenter = createServiceCenter(user);

        String newName = "Updated Service Center Name";
        serviceCenter.setName(newName);
        serviceCenter.setAverageRating(4.0f);

        serviceCenterRepository.save(serviceCenter);

        Optional<ServiceCenter> updatedServiceCenter = serviceCenterRepository.findById(serviceCenter.getId());
        assertTrue(updatedServiceCenter.isPresent());
        assertEquals(newName, updatedServiceCenter.get().getName());
        assertEquals(4.0f, updatedServiceCenter.get().getAverageRating());
    }


    @Test
    void deleteServiceCenter() {
        User user = userRepository.findById(1L).orElse(null);
        assertNotNull(user);

        ServiceCenter serviceCenter = createServiceCenter(user);

        serviceCenterRepository.save(serviceCenter);

        assertTrue(serviceCenterRepository.existsById(serviceCenter.getId()));

        serviceCenterRepository.deleteById(serviceCenter.getId());

        assertFalse(serviceCenterRepository.existsById(serviceCenter.getId()));
    }


    @Test
    void existsByAddress() {
        User user = userRepository.findById(1L).orElse(null);
        assertNotNull(user);

        ServiceCenter serviceCenter = createServiceCenter(user);
        serviceCenterRepository.save(serviceCenter);

        boolean existsServiceCenter = serviceCenterRepository.existsByAddress(createAddress());
        assertTrue(existsServiceCenter);
    }
    @Test
    void saveServiceCenterWithIncompleteData() {
        User user = userRepository.findById(1L).orElse(null);
        assertNotNull(user);

        ServiceCenter serviceCenter = createServiceCenter(user);
        serviceCenter.setName("");

        assertThrows(Exception.class, () -> {
            serviceCenterRepository.save(serviceCenter);
        });
    }
}