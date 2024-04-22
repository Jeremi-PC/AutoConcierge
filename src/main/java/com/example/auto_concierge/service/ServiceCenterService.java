package com.example.auto_concierge.service;

import com.example.auto_concierge.entity.user.Role;
import com.example.auto_concierge.entity.serviceCenter.ServiceCenter;
import com.example.auto_concierge.entity.user.User;
import com.example.auto_concierge.repository.ServiceCenterRepository;
import com.example.auto_concierge.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ServiceCenterService {
    // Радиус Земли в километрах
    private final ServiceCenterRepository serviceCenterRepository;
    private static final double earthRadius = 6371.0;
    private static final double unknownKoefficient = 6371.0;

    private final UserRepository userRepository;

    @Autowired
    public ServiceCenterService(ServiceCenterRepository serviceCenterRepository, UserRepository userRepository) {
        this.serviceCenterRepository = serviceCenterRepository;
        this.userRepository = userRepository;
    }

    public ServiceCenter createServiceCenter(Long userId, ServiceCenter serviceCenter) {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new RuntimeException("Пользователь с идентификатором " + userId + " не найден");
        }
        if (user.getRole() == Role.SERVICE_CENTER) {
            if (serviceCenterRepository.existsByAddress(serviceCenter.getAddress())) {
                throw new RuntimeException("СТО с таким адресом уже существует");
            }
            serviceCenter.setOwner(user);
            return serviceCenterRepository.save(serviceCenter);
        } else {
            throw new RuntimeException("Недостаточно прав для создания сервис центра");
        }
    }

    public List<ServiceCenter> getAllServiceCenters() {
        List<ServiceCenter> serviceCenters = serviceCenterRepository.findAll();
        if (serviceCenters.isEmpty()) {
            throw new RuntimeException("Сервисные центры не найдены");
        }
        return serviceCenters;
    }

    public ServiceCenter getServiceCenterById(Long id) {
        Optional<ServiceCenter> optionalServiceCenter = serviceCenterRepository.findById(id);
        if (optionalServiceCenter.isEmpty()) {
            throw new IllegalArgumentException("Сервисный центр с идентификатором " + id + " не найден");
        }
        return serviceCenterRepository.findById(id).orElse(null);
    }

    public ServiceCenter updateServiceCenter(Long id, ServiceCenter serviceCenterDetails) {
        ServiceCenter serviceCenter = serviceCenterRepository.findById(id).orElse(null);
        if (serviceCenter == null) {
            throw new IllegalArgumentException("Сервисный центр с идентификатором " + id + " не найден");
        }
            serviceCenter.setName(serviceCenterDetails.getName());
            serviceCenter.setAddress(serviceCenterDetails.getAddress());
            serviceCenter.setContactNumber(serviceCenterDetails.getContactNumber());
            serviceCenter.setWebsite(serviceCenterDetails.getWebsite());
            serviceCenter.setAverageRating(serviceCenterDetails.getAverageRating());
            serviceCenter.setSchedule(serviceCenterDetails.getSchedule());
            return serviceCenterRepository.save(serviceCenter);
    }

    public void deleteServiceCenter(Long id) {
        if (!serviceCenterRepository.existsById(id)) {
            throw new IllegalArgumentException("Сервисный центр с идентификатором " + id + " не найден");
        }
        try {
            serviceCenterRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при удалении сервисного центра с идентификатором " + id + ": " + e.getMessage(), e);
        }
    }

    public List<ServiceCenter> findServiceCentersWithinRadius(double latitude, double longitude, double radius) {
        List<ServiceCenter> serviceCenters = serviceCenterRepository.findAll();
        List<ServiceCenter> serviceCentersWithinRadius = new ArrayList<>();
        double radiusInKm = radius * unknownKoefficient;
        for (ServiceCenter serviceCenter : serviceCenters) {
            double distance = getDistance(latitude, longitude, serviceCenter);
            if (distance <= radiusInKm) {
                serviceCentersWithinRadius.add(serviceCenter);
            }
        }
        return serviceCentersWithinRadius;
    }

    private static double getDistance(double latitude, double longitude, ServiceCenter serviceCenter) {
        double serviceCenterLat = Math.toRadians(serviceCenter.getAddress().getLatitude());
        double serviceCenterLon = Math.toRadians(serviceCenter.getAddress().getLongitude());
        double deltaLat = Math.toRadians(latitude - serviceCenterLat);
        double deltaLon = Math.toRadians(longitude - serviceCenterLon);

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(serviceCenterLat) * Math.cos(latitude) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = earthRadius * c;
        return distance;
    }
}

