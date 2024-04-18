package com.example.auto_concierge.service;

import com.example.auto_concierge.dto.serviceCenter.ServiceCenterDTO;
import com.example.auto_concierge.entity.user.Role;
import com.example.auto_concierge.entity.serviceCenter.ServiceCenter;
import com.example.auto_concierge.entity.user.User;
import com.example.auto_concierge.mapper.ServiceCenterMapper;
import com.example.auto_concierge.repository.ServiceCenterRepository;
import com.example.auto_concierge.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ServiceCenterService {
    // Радиус Земли в километрах
    private final ServiceCenterRepository serviceCenterRepository;
    private final ServiceCenterMapper serviceCenterMapper;
    private static final double earthRadius = 6371.0;
    private static final double unknownKoefficient = 6371.0;

    private final UserRepository userRepository;

    @Autowired
    public ServiceCenterService(ServiceCenterRepository serviceCenterRepository, ServiceCenterMapper serviceCenterMapper, UserRepository userRepository) {
        this.serviceCenterRepository = serviceCenterRepository;
        this.serviceCenterMapper = serviceCenterMapper;
        this.userRepository = userRepository;
    }

    public ServiceCenter createServiceCenter(Long userId, ServiceCenter serviceCenter) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null && user.getRole() == Role.SERVICE_CENTER) {
            if (serviceCenterRepository.existsByAddress(serviceCenter.getAddress())) {
                throw new RuntimeException("СТО с таким адресом уже существует");
            }
            serviceCenter.setOwner(user);
            return serviceCenterRepository.save(serviceCenter);
        } else {
            throw new RuntimeException("Недостаточно прав для создания сервис центра или пользователь с указанным ID не найден");
        }
    }

    public List<ServiceCenter> getAllServiceCenters() {
        return serviceCenterRepository.findAll();
    }

    public ServiceCenterDTO getServiceCenterDTOById(Long id) {
        ServiceCenter serviceCenter = serviceCenterRepository.findById(id).orElse(null);
        if (serviceCenter != null) {
            return serviceCenterMapper.serviceCenterToServiceCenterDto(serviceCenter);
        } else {
            return null;
        }
    }
    public ServiceCenter getServiceCenterById(Long id) {
        return serviceCenterRepository.findById(id).orElse(null);
    }

    public ServiceCenter updateServiceCenter(Long id, ServiceCenter serviceCenterDetails) {
        ServiceCenter serviceCenter = serviceCenterRepository.findById(id).orElse(null);
        if (serviceCenter != null) {
            serviceCenter.setName(serviceCenterDetails.getName());
            serviceCenter.setAddress(serviceCenterDetails.getAddress());
            serviceCenter.setContactNumber(serviceCenterDetails.getContactNumber());
            serviceCenter.setWebsite(serviceCenterDetails.getWebsite());
            serviceCenter.setAverageRating(serviceCenterDetails.getAverageRating());
            serviceCenter.setSchedule(serviceCenterDetails.getSchedule());
            return serviceCenterRepository.save(serviceCenter);
        } else {
            throw new RuntimeException("Service center not found");
        }
    }

    public void deleteServiceCenter(Long id) {
        serviceCenterRepository.deleteById(id);
    }

    public List<ServiceCenter> findServiceCentersWithinRadius(double latitude, double longitude, double radius) {
        List<ServiceCenter> serviceCenters = serviceCenterRepository.findAll();
        List<ServiceCenter> serviceCentersWithinRadius = new ArrayList<>();
        double radiusInKm = radius * unknownKoefficient;
        for (ServiceCenter serviceCenter : serviceCenters) {
            double serviceCenterLat = Math.toRadians(serviceCenter.getAddress().getLatitude());
            double serviceCenterLon = Math.toRadians(serviceCenter.getAddress().getLongitude());
            double deltaLat = Math.toRadians(latitude - serviceCenterLat);
            double deltaLon = Math.toRadians(longitude - serviceCenterLon);

            double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                    Math.cos(serviceCenterLat) * Math.cos(latitude) *
                            Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

            double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

            double distance = earthRadius * c;

            // Если расстояние меньше или равно заданному радиусу, добавляем сервис-центр в список
            if (distance <= radiusInKm) {
                serviceCentersWithinRadius.add(serviceCenter);
            }
        }

        return serviceCentersWithinRadius;
    }
}

